    private void setupChannels() throws IOException, SAPIException {
        String[] channelList = controlPort.getChannels();
        String[] unitList = controlPort.getNwpUnits(channelList);
        log.debug("unit list length: " + unitList.length + " channel list length: " + channelList.length);
        log.info("Preparing to listen to NWP Channels: ");
        for (int i = 0; i < channelList.length; i++) {
            log.info("Setup channel: " + channelList[i] + " with units: " + unitList[i]);
            channelUnitHash.put(channelList[i], unitList[i]);
        }
        controlPort.getSensorMetadata(channelList, sensTypeHash, measTypeHash);
        if (unitList.length == channelList.length) {
            postMetadata(channelList, unitList);
        } else {
            log.warn("Channel list and unit list are of different lengths, units not posted!");
        }
        for (int i = 0; i < channelList.length; i++) {
            listener.registerChannel(channelList[i]);
        }
    }
