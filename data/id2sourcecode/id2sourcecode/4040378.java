    public void configureSensor(LineChartSensor sensor, LineChart lineChart) {
        if (historyRequestAlreadySend.contains(sensor)) {
            return;
        }
        incSensorCount();
        double ratioD = lineChart.getHistoryCount() / 360.0;
        long ratio = ratioD < 1000.0 ? 0 : (long) ratioD;
        TimeSeries series = new TimeSeries(sensor.toString(), Millisecond.class);
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        series.setHistoryCount((int) lineChart.getHistoryCount());
        sensorSeriesEntrys.add(new SensorSeriesEntry(sensor.getSensorId(), series, ratio));
        Log.logInfo("client", "LineChartDisplay.configureSensor", "Ratio for sensor <" + sensor.getSensorId() + ">: " + ratio);
        XYPlot plot = chart.getXYPlot();
        int sensorNr = sensor.getSensorNr();
        int mappedAxis = sensorNr;
        if (sensor.getAxisRangeMode() != LineChartSensor.MODE_TAKEFROM) {
            NumberAxis axis = new NumberAxis();
            plot.setRangeAxis(sensorNr, axis);
            axis.setLabel(lineChart.showRangeLabels() ? sensor.getFormattedAxisLabel() : "");
            axis.setLabelPaint(Color.black);
            axis.setTickLabelsVisible(lineChart.showRangeTickLabels());
            axis.setTickLabelPaint(Color.black);
            axis.setTickLabelFont(Font.decode(lineChart.getFont()));
            axis.setLabelFont(Font.decode(lineChart.getFont()));
            switch(sensor.getAxisRangeMode()) {
                case LineChartSensor.MODE_AUTO:
                    {
                        axis.setRange(-5, 5);
                        axis.setAutoRange(true);
                        axis.setAutoRangeIncludesZero(false);
                        break;
                    }
                case LineChartSensor.MODE_MANUAL:
                    {
                        axis.setRange(sensor.getAxisRangeStart(), sensor.getAxisRangeStop());
                        break;
                    }
            }
        } else {
            mappedAxis = sensor.getAxisTakeFrom();
        }
        XYItemRenderer renderer = new StandardXYItemRenderer();
        renderer.setSeriesPaint(0, new Color(sensor.getColor()));
        plot.setRenderer(sensorNr, renderer);
        plot.setDataset(sensorNr, dataset);
        plot.mapDatasetToRangeAxis(sensorNr, mappedAxis);
        historyRequestAlreadySend.add(sensor);
        double channelNumber = AppContext.instance().getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        final HistoricalDataRequestAction historyDataServerAction = new HistoricalDataRequestAction(lineChart.getUniqueId(), sensor.getStationId(), sensor.getSensorId(), sensor.getStartDate(), sensor.getStopDate());
        historyDataServerAction.setTransceiver(clientTransceiver);
        final ActionProcessorRegistry actionProcessorRegistry = Engine.instance().getActionProcessorRegistry();
        CommandTools.performAsync(new Command("historyrequest") {

            public boolean canPerform() {
                return true;
            }

            public void perform() {
                ActionTools.sendToServer(historyDataServerAction);
            }
        });
    }
