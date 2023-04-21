    public boolean removeChannel(String channelName) {
        if (xyMode) {
            if (!channels.contains(channelName)) {
                return false;
            }
            int channelIndex = channels.indexOf(channelName);
            String firstChannel, secondChannel;
            if (channelIndex % 2 == 0) {
                firstChannel = channelName;
                if (channelIndex + 1 < channels.size()) {
                    secondChannel = (String) channels.get(channelIndex + 1);
                } else {
                    secondChannel = null;
                }
            } else {
                firstChannel = (String) channels.get(channelIndex - 1);
                secondChannel = channelName;
            }
            rbnbController.unsubscribe(firstChannel, this);
            channels.remove(firstChannel);
            if (secondChannel != null) {
                rbnbController.unsubscribe(secondChannel, this);
                channels.remove(secondChannel);
                String firstChannelDisplay = getChannelDisplay(firstChannel);
                String secondChannelDisplay = getChannelDisplay(secondChannel);
                String seriesName = firstChannelDisplay + " vs. " + secondChannelDisplay;
                XYTimeSeriesCollection dataCollection = (XYTimeSeriesCollection) this.dataCollection;
                XYTimeSeries data = dataCollection.getSeries(seriesName);
                dataCollection.removeSeries(data);
                colors.remove(seriesName);
            }
            channelRemoved(channelName);
            return true;
        } else {
            return super.removeChannel(channelName);
        }
    }
