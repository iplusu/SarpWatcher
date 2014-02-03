/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sarpwatcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Frame;
import javax.swing.BorderFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.data.time.FixedMillisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class TrustChart extends Frame
{
    public int source, target;
    public TimeSeriesCollection[] datasets;
    private double[] lastValue = new double[3];

    public TrustChart(String paramString, int source, int target)
    {
        super(paramString);

        this.source = source;
        this.target = target;

        DateAxis transaction = new DateAxis("Time");
        transaction.setAutoRange(true);
        transaction.setAutoRangeMinimumSize(5);
        transaction.setUpperMargin(0.05D);
        transaction.setLowerMargin(0.05D);

        CombinedDomainXYPlot localCombinedDomainXYPlot = new CombinedDomainXYPlot(transaction);
        this.datasets = new TimeSeriesCollection[3];
        for (int i = 0; i < 3; ++i)
        {
            this.lastValue[i] = 100.0D;
            TimeSeries localXYSeries1 = null;
            switch (i) {
            case 0:
                localXYSeries1 = new TimeSeries("Forward");
                break;
            /*case 1:
                localXYSeries1 = new TimeSeries("Loop");
                break;
             */
            case 1:
                localXYSeries1 = new TimeSeries("Predictable");
                break;
            case 2:
                localXYSeries1 = new TimeSeries("Overall");
                break;
            default:
                localXYSeries1 = null;
                break;
            }
            this.datasets[i] = new TimeSeriesCollection(localXYSeries1);

            NumberAxis localNumberAxis;
            if (i == 0) {
                localNumberAxis = new NumberAxis("Foward");
                localNumberAxis.setRangeWithMargins(-0.2D, 1.2D);
            /*} else if (i == 1) {
                localNumberAxis = new NumberAxis("Loop");
                localNumberAxis.setRangeWithMargins(-0.2D, 1.2D);
             */
            } else if (i == 1) {
                localNumberAxis = new NumberAxis("Predictable");
                localNumberAxis.setRangeWithMargins(-0.2D, 1.2D);
            } else {
                localNumberAxis = new NumberAxis("Overall");
                localNumberAxis.setRangeWithMargins(-0.2D, 1.2D);
            }

            XYPlot localXYPlot = new XYPlot(this.datasets[i], null, localNumberAxis, new StandardXYItemRenderer());

            XYItemRenderer xyItemRenderer = localXYPlot.getRenderer();
            ((AbstractRenderer)xyItemRenderer).setAutoPopulateSeriesStroke(false);
            xyItemRenderer.setBaseStroke(new BasicStroke(3.0F));
            xyItemRenderer.setSeriesStroke(0, new BasicStroke(3.0F));
            xyItemRenderer.setSeriesStroke(1, new BasicStroke(3.0F));
            xyItemRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
            xyItemRenderer.setBaseItemLabelsVisible(true);

            localXYPlot.setBackgroundPaint(Color.lightGray);
            localXYPlot.setDomainGridlinePaint(Color.white);
            localXYPlot.setRangeGridlinePaint(Color.white);
            localCombinedDomainXYPlot.add((XYPlot)localXYPlot);
        }

        String title = new String();
        title = String.format("Trust Chart Source %d / Target : %d",source, target);
        JFreeChart localJFreeChart = new JFreeChart(title, localCombinedDomainXYPlot);
        LegendTitle localLegendTitle = (LegendTitle)localJFreeChart.getSubtitle(0);
        localLegendTitle.setPosition(RectangleEdge.RIGHT);
        localLegendTitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4.0D, 0.0D, 4.0D));
        localJFreeChart.setBorderPaint(Color.black);
        localJFreeChart.setBorderVisible(true);

        ChartUtilities.applyCurrentTheme(localJFreeChart);
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);

        localChartPanel.setPreferredSize(new Dimension(500, 500));
        localChartPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.add(localChartPanel);
        this.pack();
        this.setVisible(true);
    }

    public void addValues(int transaction, long time, float trustFP, float trustPR, float overall) {

        FixedMillisecond sec = new FixedMillisecond(time);
        datasets[0].getSeries(0).addOrUpdate(sec, trustFP);
//        datasets[3].getSeries(0).add(sec, trustLP);
        datasets[1].getSeries(0).addOrUpdate(sec, trustPR);
        datasets[2].getSeries(0).addOrUpdate(sec, overall);
    }
}
