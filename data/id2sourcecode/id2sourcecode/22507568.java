    public void plot() {
        cat.debug("filename to read: " + filename);
        if (csvTable == null) csvTable = new gov.lanl.translate.CSVTable(filename, "/csv");
        if (chart == null) chart = new ServerChart();
        ConfigProperties p = new ConfigProperties(props);
        Enumeration keys = p.keys();
        while (keys.hasMoreElements()) {
            String prop = (String) keys.nextElement();
            String value = p.getProperty(prop);
            gov.lanl.Utility.BeanProperties.setProperty(chart, prop, value);
        }
        String[] labels = csvTable.getLabels();
        if (labels.length == 0) {
            cat.warn("NO Data read from " + filename + "!");
            return;
        }
        String[] x = csvTable.getValues(labels[0]);
        double[] dat = new double[x.length];
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy");
        java.text.SimpleDateFormat dform = new java.text.SimpleDateFormat("MM/dd");
        JCAxis xAxis = chart.getChartArea().getXAxis(0);
        xAxis.setTimeUnit(JCAxis.MONTHS);
        xAxis.setAnnotationMethod(JCAxis.TIME_LABELS);
        chart.getLegend().setVisible(true);
        JCAxis yAxis = chart.getChartArea().getYAxis(0);
        yAxis.getTitle().setPlacement(JCLegend.WEST);
        yAxis.getTitle().setRotation(ChartText.DEG_270);
        try {
            for (int k = 0; k < x.length; k++) {
                Date d = df.parse(x[k]);
                if (k == 0) xAxis.setTimeBase(d);
                dat[k] = xAxis.dateToValue(d);
            }
            double[][] xx = new double[1][dat.length];
            xx[0] = dat;
            String[] labs = new String[first];
            double[][] zz = new double[first][dat.length];
            for (int i = 0; i < first; i++) {
                String[] v = csvTable.getValues(labels[i + 1]);
                zz[i] = new double[v.length];
                labs[i] = labels[i + 1];
                for (int k = 0; k < v.length; k++) {
                    zz[i][k] = new Double(v[k]).doubleValue();
                }
            }
            ChartDataView dataView = chart.getDataView(0);
            dataView.setDataSource(new JCDefaultDataSource(xx, zz, null, labs, ""));
            dataView.setChartType(JCChart.BAR);
            ((JCBarChartFormat) dataView.getChartFormat()).setClusterWidth(barWidth);
            double[][] yy = new double[labels.length - first - 1][];
            labs = new String[labels.length - first - 1];
            for (int i = first + 1; i < labels.length; i++) {
                String[] v = csvTable.getValues(labels[i]);
                labs[i - first - 1] = labels[i];
                yy[i - first - 1] = new double[v.length];
                for (int k = 0; k < v.length; k++) {
                    double f = new Double(v[k]).doubleValue();
                    yy[i - first - 1][k] = f;
                }
            }
            chart.addDataView(1);
            dataView = chart.getDataView(1);
            dataView.setDataSource(new JCDefaultDataSource(xx, yy, null, labs, ""));
            if (plotType.equals("line")) dataView.setChartType(JCChart.PLOT); else dataView.setChartType(JCChart.STACKING_AREA);
        } catch (Exception e) {
            cat.error("Plot failed due to :" + e, e);
        }
    }
