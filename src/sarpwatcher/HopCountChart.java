/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

/**
 *
 * @author Administrator
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.ui.TextAnchor;

public class HopCountChart extends Frame
{
    private XYSeries series = new XYSeries("Hop Counts");
    //private XYDataset series;
    private JFreeChart localJFreeChart;
    private XYPlot localXYPlot;
    private XYItemRenderer xyItemRenderer;
    private double yAxis = 20D;
    private Color noteColor = Color.blue;

    public HopCountChart(String paramString)
    {
        super(paramString);
        XYSeriesCollection localXYSeriesCollection = new XYSeriesCollection(this.series);
        ChartPanel localChartPanel = new ChartPanel(createChart(localXYSeriesCollection));
        localChartPanel.setPreferredSize(new Dimension(500, 270));

        this.add(localChartPanel);
        //this.add(localChartPanel, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private JFreeChart createChart(XYDataset paramXYDataset) {
        localJFreeChart = ChartFactory.createXYLineChart("Hop Count History", "Count", "Counts", paramXYDataset, PlotOrientation.VERTICAL, false, true, false);
        localXYPlot = (XYPlot)localJFreeChart.getPlot();
        localXYPlot.setDomainPannable(true);
        localXYPlot.setRangePannable(true);

        ValueAxis localValueAxis = (NumberAxis)localXYPlot.getDomainAxis();
        //ValueAxis localValueAxis = new NumberAxis("Hop Counts");
        localValueAxis.setAutoRange(true);
        localValueAxis.setAutoRangeMinimumSize(5);
        //localValueAxis.setAutoRangeIncludesZero(false);
        //localValueAxis.setAutoRangeStickyZero(false);
        //localValueAxis.setFixedAutoRange(50.0D);
        //localValueAxis = localXYPlot.getRangeAxis();
        localValueAxis = new NumberAxis("Hop Counts");
        localValueAxis.setRangeWithMargins(-1.0D, 6.0D);
        localXYPlot.setRangeAxis(localValueAxis);
        

        xyItemRenderer = localXYPlot.getRenderer();
        ((AbstractRenderer)xyItemRenderer).setAutoPopulateSeriesStroke(false);
        xyItemRenderer.setBaseStroke(new BasicStroke(3.0F));
        xyItemRenderer.setSeriesStroke(0, new BasicStroke(2.0F));
        xyItemRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
        xyItemRenderer.setBaseItemLabelsVisible(true);

        return localJFreeChart;
    }

    public void addValues(double count, double percentage) {
        //System.out.println(count + " " + percentage);
        this.series.addOrUpdate(count, percentage);
    }

    public void addNotation(String str, double transaction){
        if(yAxis == 15D)   yAxis = 20D;
        else                yAxis = 15D;
        XYPointerAnnotation notation = new XYPointerAnnotation(str, transaction, yAxis, -1.7D);
        notation.setTextAnchor(TextAnchor.BOTTOM_CENTER);
        if(noteColor == Color.blue) noteColor = Color.red;
        else                        noteColor = Color.blue;
        notation.setPaint(noteColor);
        notation.setArrowPaint(noteColor);
        
        xyItemRenderer.addAnnotation(notation);
    }
}
