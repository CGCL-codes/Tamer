    public void addSourceChannels(String sourceName, TurbineServer server) {
        SensorMetaData smd = SensorMetaDataManager.getInstance().getSensorMetaDataIfPresent(sourceName);
        if (smd == null) {
            Debugger.debug(Debugger.RECORD, "Requesting Meta data for \"" + sourceName + "\"");
            SiteMetaDataRequester mdr = new SiteMetaDataRequester(sourceName);
            smd = mdr.call();
        }
        Vector<String> completeChannelStrs = new Vector<String>();
        Vector<Integer> channelDatatypeVec = new Vector<Integer>();
        Vector<Integer> reqModeVec = new Vector<Integer>();
        Vector<Integer> intervalOrToutVec = new Vector<Integer>();
        Vector<String> actChannelsVec = smd.getChannels();
        for (int i = 0; i < actChannelsVec.size(); i++) {
            completeChannelStrs.addElement(sourceName + "/" + actChannelsVec.elementAt(i));
            reqModeVec.addElement(new Integer(TurbineSinkConfig.MONITOR_MODE));
            intervalOrToutVec.addElement(new Integer(-1));
        }
        server.resetSinkWrapperChannelVecs(completeChannelStrs, smd.getChannelDatatypes(), reqModeVec, intervalOrToutVec);
    }
