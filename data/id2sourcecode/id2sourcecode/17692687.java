    private void addLocalSeries() {
        if (chooser == null) {
            chooser = new JFileChooser();
        }
        int returnVal = chooser.showOpenDialog(getDataComponent());
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        if (file == null || !file.isFile() || !file.exists()) {
            return;
        }
        DataFileReader reader;
        try {
            reader = new DataFileReader(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(getDataComponent(), e.getMessage(), "Problem reading data file", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<DataChannel> channels = reader.getChannels();
        if (channels.size() < 2) {
            JOptionPane.showMessageDialog(getDataComponent(), "There must be at least 2 channels in the data file", "Problem with data file", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DataChannel xChannel;
        DataChannel yChannel;
        if (channels.size() == 2) {
            xChannel = channels.get(0);
            yChannel = channels.get(1);
        } else {
            xChannel = (DataChannel) JOptionPane.showInputDialog(getDataComponent(), "Select the x channel:", "Add local channel", JOptionPane.PLAIN_MESSAGE, null, channels.toArray(), null);
            if (xChannel == null) {
                return;
            }
            yChannel = (DataChannel) JOptionPane.showInputDialog(getDataComponent(), "Select the y channel:", "Add local channel", JOptionPane.PLAIN_MESSAGE, null, channels.toArray(), null);
            if (yChannel == null) {
                return;
            }
        }
        String xChannelName = xChannel.getName();
        if (xChannel.getUnit() != null) {
            xChannelName += " (" + xChannel.getUnit() + ")";
        }
        int xChannelIndex = channels.indexOf(xChannel);
        String yChannelName = yChannel.getName();
        if (yChannel.getUnit() != null) {
            yChannelName += " (" + yChannel.getUnit() + ")";
        }
        int yChannelIndex = channels.indexOf(yChannel);
        String seriesName = xChannelName + " vs. " + yChannelName;
        XYTimeSeries data = new XYTimeSeries(seriesName, FixedMillisecond.class);
        try {
            NumericDataSample sample;
            while ((sample = reader.readSample()) != null) {
                double timestamp = sample.getTimestamp();
                Number[] values = sample.getValues();
                FixedMillisecond time = new FixedMillisecond((long) (timestamp * 1000));
                XYTimeSeriesDataItem dataItem = new XYTimeSeriesDataItem(time);
                if (values[xChannelIndex] != null && values[yChannelIndex] != null) {
                    dataItem.setX(values[xChannelIndex]);
                    dataItem.setY(values[yChannelIndex]);
                }
                data.add(dataItem, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Color color = getLeastUsedColor();
        colors.put(seriesName, color);
        ((XYTimeSeriesCollection) dataCollection).addSeries(data);
        localSeries++;
        setSeriesColors();
        updateTitle();
        updateLegend();
    }
