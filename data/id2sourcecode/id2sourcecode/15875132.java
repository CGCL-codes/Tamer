    public void goToCue(float cueNumber) {
        stopTransition();
        channelValues.removeAllCues();
        channelValues.addSet(new CueValueSet(show.getCue(cueNumber)));
        Channel[] channels = show.getCue(cueNumber).getAllChannels();
        try {
            dmxDevice.setValues(channels);
        } catch (IDMXDeviceException e) {
            writeError(e);
            return;
        }
        connector.updateChannels(channels, channelValues.getChannelSources(channels));
    }
