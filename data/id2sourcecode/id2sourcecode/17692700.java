    @Override
    protected JComponent getChannelComponent() {
        if (xyMode) {
            int remoteSeries = dataCollection.getSeriesCount() - localSeries;
            if (channels.size() == 0 && localSeries == 0) {
                return null;
            }
            JPanel titleBar = new JPanel();
            titleBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            titleBar.setOpaque(false);
            if (isShowChannelsInTitle()) {
                Iterator<String> i = channels.iterator();
                while (i.hasNext()) {
                    String firstChannel = i.next();
                    String series = firstChannel;
                    if (i.hasNext()) {
                        series += " vs. " + i.next();
                    }
                    titleBar.add(new ChannelTitle(series, firstChannel));
                }
                for (int j = remoteSeries; j < remoteSeries + localSeries; j++) {
                    String seriesName = (String) dataCollection.getSeriesKey(j);
                    titleBar.add(new LocalChannelTitle(seriesName));
                }
            }
            return titleBar;
        } else {
            return super.getChannelComponent();
        }
    }
