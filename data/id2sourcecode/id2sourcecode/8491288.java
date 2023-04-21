    public StateSensorPlot(StateSensor sensor, ServerContext serverContext) {
        super((Sensor) sensor, serverContext);
        for (int channel = 0; channel < sensor.getNumChannels(); channel++) {
            timeseriesCollection.addSeries(new TimeSeries(sensor.getChannelDescription(channel) + " [CH-" + channel + "]", org.jfree.data.time.Second.class));
        }
        plotData(this.sensor.getCurrentData());
    }
