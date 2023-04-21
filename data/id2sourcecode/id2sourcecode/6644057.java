    public void testChartCreation() throws Exception {
        String chartType = "pie";
        List series1 = new ArrayList();
        List series2 = new ArrayList();
        List labels = new ArrayList();
        series1.add(new Integer(10));
        series1.add(new Integer(20));
        series1.add(new Integer(30));
        series1.add(new Integer(40));
        series2.add(new Float(22.3));
        series2.add(new Float(45.0));
        series2.add(new Float(12.5));
        series2.add(new Float(18.8));
        labels.add("Label 1");
        labels.add("Label 2");
        labels.add("Label 3");
        labels.add("Label 4");
        ChartDefinition chartDefinition = new ChartDefinition();
        Color fgColors[] = chartDefinition.getFgColors();
        int size = "pie".equals(chartType) ? 4 : 3;
        ListDataBuffer[] buffers = new ListDataBuffer[size];
        buffers[0] = new ListDataBuffer(new AutoType("value", "test1", null), series1);
        buffers[1] = new ListDataBuffer(new AutoType("value", "test2", null), series2);
        buffers[2] = new ListDataBuffer(new TextType("label"), labels);
        if ("pie".equals(chartType)) {
            StringBuffer colors = new StringBuffer();
            for (int i = 0; i < fgColors.length; i++) {
                Color color = fgColors[i];
                colors.append(Convertors.colorToCSSColor(color)).append(' ');
            }
            buffers[3] = new ListDataBuffer(new PaintType("paint"), colors.toString(), ' ');
        }
        JoinedDataBuffer buffer = new JoinedDataBuffer(null, buffers, null);
        ChartData chartData = new ChartData(buffer);
        com.davisor.graphics.chart.ChartObjectAttributes chartAttributes = ChartAttributesFactory.getInstance().createChartObjectAttributes();
        chartAttributes.setChartType(chartType);
        chartAttributes.setWidth(new Integer(320), true);
        chartAttributes.setHeight(new Integer(240), true);
        chartAttributes.setContentType("image/jpeg");
        PlotRenderAttributes defaultPlotRenderAttributes = chartAttributes.getPlot();
        defaultPlotRenderAttributes.setValueFormat("{.value,float,#}");
        defaultPlotRenderAttributes.setFont(new Font("Arial", Font.PLAIN, 14));
        defaultPlotRenderAttributes.addChannelAttributes(chartData);
        ChannelAttributes channelAttributes = (ChannelAttributes) defaultPlotRenderAttributes.getChannelAttributes().get("test1");
        channelAttributes.setName("TEST 1");
        channelAttributes = (ChannelAttributes) defaultPlotRenderAttributes.getChannelAttributes().get("test2");
        channelAttributes.setName("TEST 2");
        ChartAxes chartAxes = (ChartAxes) chartAttributes.getAxes().get(0);
        ChartAxis xAxis = chartAxes.getAxis(0);
        xAxis.setTitleText("xTitle");
        RenderAttributes axisRender = xAxis.getRender();
        axisRender.setColor(Color.magenta);
        axisRender.setFont(new Font("Arial", Font.PLAIN, 14));
        ChartAxis yAxis = chartAxes.getAxis(1);
        yAxis.setTitleText("yTitle");
        axisRender = yAxis.getRender();
        axisRender.setColor(Color.cyan);
        axisRender.setFont(new Font("Arial", Font.PLAIN, 14));
        PlotRenderAttributes render = chartAxes.getPlot();
        render.setDefaults(defaultPlotRenderAttributes);
        ChartFactory factory = ChartFactory.getFactory(chartType);
        ImageChart chart = (ImageChart) factory.createChart(chartData, chartAttributes);
        String filename = TEMP_DIR + File.separator + "test.jpeg";
        chart.putImage(filename);
        File file = new File(filename);
        assertTrue("Chart file should now exist: " + filename, file.exists());
        file.delete();
        assertFalse("Chart file should now be deleted: " + filename, file.exists());
    }
