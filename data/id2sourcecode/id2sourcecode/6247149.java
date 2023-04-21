    public XMLBarChartDemo(final String title) {
        super(title);
        CategoryDataset dataset = null;
        final URL url = getClass().getResource("/org/jfree/chart/demo/categorydata.xml");
        try {
            final InputStream in = url.openStream();
            dataset = DatasetReader.readCategoryDatasetFromXML(in);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        final JFreeChart chart = ChartFactory.createBarChart("Bar Chart", "Domain", "Range", dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.yellow);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
