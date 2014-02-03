/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;
import java.awt.BasicStroke;

import org.apache.commons.collections15.Transformer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author root
 */
public class NodeMap extends Frame {
    //enum VTYPE{NORMAL, TG, SF, LC, NR, OF};

    private Graph<Number,String> g = null;
    private VisualizationViewer<Number,String> vv = null;
    private Layout<Number,String> layout = null;
    private ArrayList<AttackArraySet> aas = new ArrayList<AttackArraySet>();
    private ParentArraySet pas;

    public NodeMap(JFrame f) {
        super("Topology Displayer");
        pas = new ParentArraySet(0);
        Dimension prefferedSize = new Dimension(500, 500);
        Graph<Number,String> ig = Graphs.<Number,String>synchronizedDirectedGraph(new DirectedSparseMultigraph<Number,String>());
        ObservableGraph<Number,String> og = new ObservableGraph<Number,String>(ig);
//        og.addGraphEventListener(new GraphEventListener<Number,String>() {
//
//            public void handleGraphEvent(GraphEvent<Number,String> evt) {
//                    System.err.println("got "+evt);
//            }});
        this.g = og;

        layout = new FRLayout<Number,String>(this.g);
        layout.setSize(prefferedSize);
        Relaxer relaxer = new VisRunner((IterativeContext)layout);
        relaxer.stop();
        relaxer.prerelax();

        Layout<Number,String> staticLayout = new StaticLayout<Number,String>(g, layout);
        final VisualizationModel<Number,String> visualizationModel = new DefaultVisualizationModel<Number,String>(staticLayout, prefferedSize);
        vv = new VisualizationViewer<Number,String>(visualizationModel, prefferedSize);

        vv.setGraphMouse(new DefaultModalGraphMouse<Number,String>());

        vv.getRenderContext().setVertexFillPaintTransformer(new MyVertexFillPaintFunction<Number>());
        vv.getRenderContext().setEdgeDrawPaintTransformer(new MyEdgePaintFunction());

        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Number>());
        vv.setForeground(Color.white);

        vv.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                super.componentResized(arg0);
//                    System.err.println("resized");
                layout.setSize(arg0.getComponent().getSize());
            }
        });

        final Stroke edgeStroke = new BasicStroke(2.0f);
        Transformer<String, Stroke> edgeStrokeTransformer =
        new Transformer<String, Stroke>() {
            public Stroke transform(String s) {
                return edgeStroke;
            }
        };
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);

        setLayout(new BorderLayout());
        setBackground(java.awt.Color.lightGray);
        setFont(new Font("Serif", Font.PLAIN, 12));
        add(vv, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(f.getHeight(), f.getHeight()));
        this.setLocation(f.getX() + f.getWidth(), f.getY());
        this.pack();
        this.setVisible(true);

//        vv.repaint();
    }

    public void redraw() {
        if (vv != null)
            this.vv.repaint();
    }

    public void addNode(int node) {
        this.g.addVertex(node);
    }

    public void addEdge(int sNode, int tNode) {
        String edge = String.format("%dto%d", sNode, tNode);
        this.g.addEdge(edge, sNode, tNode);
        this.layout.initialize();

        Relaxer relaxer = new VisRunner((IterativeContext)this.layout);
        relaxer.stop();
        relaxer.prerelax();
        vv.repaint();
    }

    public void setToParent(ArrayList<AttackArraySet> aas, ParentArraySet pas) {
        this.pas = pas;
        this.aas = aas;
        for(int i = 0; i < pas.size(); i++)
            vv.getRenderer().renderEdge(vv.getRenderContext(), layout, pas.NodeMapString(i));
        vv.repaint();
    }

    public void finalizeGraph() {
        setVertexColor(aas, 0);
        StaticLayout<Number,String> staticLayout = new StaticLayout<Number,String>(this.g, this.layout);
        LayoutTransition<Number,String> lt = new LayoutTransition<Number,String>(this.vv, this.vv.getGraphLayout(), staticLayout);
        Animator animator = new Animator(lt);
        animator.start();
        vv.repaint();
    }

    public void setVertexColor(ArrayList aas, Number v) {
        this.aas = aas;
        Point2D p = layout.transform(v);
        p = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT, p);
        vv.repaint();
    }

    public void setEdgeColor(ArrayList aas, String e) {
        this.aas = aas;
        //Renderer<Number,String> renderer = vv.getRenderer();
        vv.getRenderer().renderEdge(vv.getRenderContext(), layout, e);
        vv.repaint();
    }

    public class MyVertexFillPaintFunction<V> implements Transformer<V,Paint> {
        public Paint transform( V v ) {
            for (int i = 0; i < aas.size(); i++) {
                AttackArraySet tmp = aas.get(i);
                if (0 == Integer.parseInt(v.toString())) {
                    return Color.BLACK;
                }
                if (tmp.type == AttackArraySet.TYPE.NONE && tmp.target == Integer.parseInt(v.toString())) {
                    return Color.RED;
                }
                if (tmp.type == AttackArraySet.TYPE.TRAFFICGENERATOR && tmp.target == Integer.parseInt(v.toString())) {
                    return Color.BLUE;
                }
                if (tmp.type == AttackArraySet.TYPE.SELECTIVEFORWARDER && tmp.target == Integer.parseInt(v.toString())) {
                    return Color.GRAY;
                }
                if (tmp.type == AttackArraySet.TYPE.LOOPCREATOR && tmp.target == Integer.parseInt(v.toString())) {
                    return Color.ORANGE;
                }
                if (tmp.type == AttackArraySet.TYPE.NORESPONSE && tmp.target == Integer.parseInt(v.toString())) {
                    return Color.GREEN;
                }
                if (tmp.type == AttackArraySet.TYPE.ONOFFATTACKER && tmp.target == Integer.parseInt(v.toString())) {
                    return Color.DARK_GRAY;
                }
            }
            return Color.RED;
        }
    }

    public class MyEdgePaintFunction<E> implements Transformer<String,Paint> {
        public Paint transform(String e) {
            int i = 0;
            for (i = 0; i < pas.size(); i++){
                if(e.equals(pas.NodeMapString(i)))
                    return Color.BLUE;
            }

            for (i = 0; i < aas.size(); i++) {
                AttackArraySet tmp = aas.get(i);
                if (tmp.type == AttackArraySet.TYPE.NONE && e.equals(String.format("%dto%d", tmp.target, tmp.victim))){// && !e.equals(toParent)) {
                    return Color.BLACK;
                }
                if (tmp.type == AttackArraySet.TYPE.SELECTIVEFORWARDER && e.equals(String.format("%dto%d", tmp.target, tmp.victim))){// && !e.equals(toParent)) {
                    return Color.RED;
                }
                if (tmp.type == AttackArraySet.TYPE.LOOPCREATOR && e.equals(String.format("%dto%d", tmp.target, tmp.victim))){// && !e.equals(toParent)) {
                    return Color.ORANGE;
                }
            }
            return Color.BLACK;
        }
    }
}
