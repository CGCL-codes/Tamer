    public Hashtable<String, SourceData> convertDataPktToTimeStampedTree(DataPacket dataPkt) {
        Hashtable<String, SourceData> srcHash = new Hashtable<String, SourceData>();
        Hashtable<String, Integer> recentNumPkts = new Hashtable<String, Integer>();
        for (int i = 0; i < dataPkt.getSize(); i++) {
            Object data = dataPkt.getDataAt(i);
            String chanName = dataPkt.getChannelNameAt(i);
            int index = chanName.indexOf("/");
            String source = chanName.substring(0, index);
            String channel = chanName.substring(index + 1, chanName.length());
            int numPkts = 0;
            if (recentNumPkts.containsKey(source)) {
                numPkts = recentNumPkts.get(source).intValue() + 1;
                recentNumPkts.put(source, new Integer(numPkts));
            } else if (numPktsPerSrcHash.containsKey(source)) {
                numPkts = numPktsPerSrcHash.get(source).intValue() + 1;
                recentNumPkts.put(source, new Integer(numPkts));
            } else {
                recentNumPkts.put(source, new Integer(1));
            }
            SensorMetaData smd = SensorMetaDataManager.getInstance().getSensorMetaDataIfPresent(source);
            if (smd == null) {
                throw new IllegalArgumentException("No meta about the sensor \"" + source + "\" is registered");
            }
            int datatype = smd.getChannelDatatype(channel);
            if (!srcHash.containsKey(source)) {
                srcHash.put(source, new SourceData(source));
            }
            Debugger.debug(Debugger.TRACE, "Source[" + i + "]= " + source);
            Debugger.debug(Debugger.TRACE, "Channel[" + i + "]= " + channel);
            SourceData sd = srcHash.get(source);
            TimeStampedData tsData = null;
            switch(datatype) {
                case Constants.DATATYPE_DOUBLE:
                    double[] dataArr = (double[]) dataPkt.getDataAt(i);
                    Debugger.debug(Debugger.TRACE, "Data at " + i + " = " + dataArr + ": " + dataArr[0]);
                    for (int k = 0; k < dataArr.length; k++) {
                        tsData = sd.getTimeStampedData((long) (dataPkt.getTimestampAt(i, k)));
                        ChannelData chData = tsData.getChannelData(channel);
                        chData.addData(new Double(dataArr[k]));
                        Debugger.debug(Debugger.TRACE, "Adding " + dataArr[k] + " to " + channel + " (" + chData + ")");
                    }
                    break;
                default:
                    throw new IllegalStateException("Datatype of the data " + "unsupported for source/channel \"" + chanName);
            }
        }
        updateFeedbackInfo(recentNumPkts);
        return srcHash;
    }
