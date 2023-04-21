package testes.jfreechart;

import java.awt.Font;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo for the {@link CombinedRangeCategoryPlot} class.
 *
 */
public class CombinedCategoryPlotDemo2 extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public CombinedCategoryPlotDemo2(final String title) {
        super(title);
        final ChartPanel chartPanel = new ChartPanel(createChart());
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    /**
     * Creates a dataset.
     *
     * @return A dataset.
     */
    public CategoryDataset createDataset1() {
        final DefaultCategoryDataset result = new DefaultCategoryDataset();
        final String series1 = "First";
        final String series2 = "Second";
        final String type1 = "Type 1";
        final String type2 = "Type 2";
        final String type3 = "Type 3";
        final String type4 = "Type 4";
        final String type5 = "Type 5";
        final String type6 = "Type 6";
        final String type7 = "Type 7";
        final String type8 = "Type 8";
        result.addValue(1.0, series1, type1);
        result.addValue(4.0, series1, type2);
        result.addValue(3.0, series1, type3);
        result.addValue(5.0, series1, type4);
        result.addValue(5.0, series1, type5);
        result.addValue(7.0, series1, type6);
        result.addValue(7.0, series1, type7);
        result.addValue(8.0, series1, type8);
        result.addValue(5.0, series2, type1);
        result.addValue(7.0, series2, type2);
        result.addValue(6.0, series2, type3);
        result.addValue(8.0, series2, type4);
        result.addValue(4.0, series2, type5);
        result.addValue(4.0, series2, type6);
        result.addValue(2.0, series2, type7);
        result.addValue(1.0, series2, type8);
        return result;
    }

    /**
     * Creates a dataset.
     *
     * @return A dataset.
     */
    public CategoryDataset createDataset2() {
        final DefaultCategoryDataset result = new DefaultCategoryDataset();
        final String series1 = "Third";
        final String series2 = "Fourth";
        final String sector1 = "Sector 1";
        final String sector2 = "Sector 2";
        final String sector3 = "Sector 3";
        final String sector4 = "Sector 4";
        result.addValue(11.0, series1, sector1);
        result.addValue(14.0, series1, sector2);
        result.addValue(13.0, series1, sector3);
        result.addValue(15.0, series1, sector4);
        result.addValue(15.0, series2, sector1);
        result.addValue(17.0, series2, sector2);
        result.addValue(16.0, series2, sector3);
        result.addValue(18.0, series2, sector4);
        return result;
    }

    /**
     * Creates a chart.
     *
     * @return A chart.
     */
    private JFreeChart createChart() {
        final CategoryDataset dataset1 = createDataset1();
        final CategoryAxis domainAxis1 = new CategoryAxis("Class 1");
        domainAxis1.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final LineAndShapeRenderer renderer1 = new LineAndShapeRenderer();
        renderer1.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        final CategoryPlot subplot1 = new CategoryPlot(dataset1, domainAxis1, null, renderer1);
        subplot1.setDomainGridlinesVisible(true);
        final CategoryDataset dataset2 = createDataset2();
        final CategoryAxis domainAxis2 = new CategoryAxis("Class 2");
        domainAxis2.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final BarRenderer renderer2 = new BarRenderer();
        renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        final CategoryPlot subplot2 = new CategoryPlot(dataset2, domainAxis2, null, renderer2);
        subplot2.setDomainGridlinesVisible(true);
        final ValueAxis rangeAxis = new NumberAxis("Value");
        final CombinedRangeCategoryPlot plot = new CombinedRangeCategoryPlot(rangeAxis);
        plot.add(subplot1, 3);
        plot.add(subplot2, 2);
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        final JFreeChart result = new JFreeChart("Combined Range Category Plot Demo", new Font("SansSerif", Font.BOLD, 12), plot, true);
        return result;
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
        final String title = "Combined Category Plot Demo 2";
        final CombinedCategoryPlotDemo2 demo = new CombinedCategoryPlotDemo2(title);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
