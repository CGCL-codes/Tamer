    public void updateIntoDb(DataPacket dataPkt) {
        Hashtable<String, SourceData> srcHash = convertDataPktToTimeStampedTree(dataPkt);
        Debugger.debug(Debugger.TRACE, "===========================================");
        Enumeration<String> enumSrcData = srcHash.keys();
        while (enumSrcData.hasMoreElements()) {
            String source = enumSrcData.nextElement();
            Debugger.debug(Debugger.TRACE, "Source=" + source);
            SensorMetaData smd = SensorMetaDataManager.getInstance().getSensorMetaData(source);
            if (smd == null) {
                throw new IllegalArgumentException("No meta about the sensor \"" + source + "\" is registered");
            }
            SourceData sd = srcHash.get(source);
            Enumeration<Long> enumTsData = sd.getTimeStamps();
            while (enumTsData.hasMoreElements()) {
                Long tsKey = enumTsData.nextElement();
                Debugger.debug(Debugger.TRACE, "\tTime=" + new Date(tsKey));
                TimeStampedData tsd = sd.getTimeStampedData(tsKey);
                String turbineTimeStr = getTimeStringForMySqlDb(tsKey.longValue());
                StringBuffer fieldNamesStr = new StringBuffer("(");
                StringBuffer valueStr = new StringBuffer("(");
                Enumeration<String> enumChannel = tsd.getChannels();
                while (enumChannel.hasMoreElements()) {
                    String channel = enumChannel.nextElement();
                    Debugger.debug(Debugger.TRACE, "\t\tChannel=" + channel);
                    if (Constants.TIMESTAMP_CHANNEL_NAME.equals(channel)) {
                        fieldNamesStr.append(Constants.LAKE_SENSORS_DB_TIME_FIELD_NAME);
                    } else {
                        fieldNamesStr = fieldNamesStr.append(channel);
                    }
                    fieldNamesStr = fieldNamesStr.append(",");
                    ChannelData cd = tsd.getChannelData(channel);
                    if (cd.getNumDataItems() > 1) {
                        throw new IllegalStateException("ERROR: Getting more than one " + "data values " + "for the same channel \"" + channel + "\" from source \"" + source + "\" at turbine time " + turbineTimeStr + ".");
                    }
                    for (int i = 0; i < cd.getNumDataItems(); i++) {
                        Debugger.debug(Debugger.TRACE, "\t\t\tDATA [ " + i + " ] = " + cd.getData(i));
                    }
                    if (Constants.TIMESTAMP_CHANNEL_NAME.equals(channel)) {
                        Double d = (Double) cd.getData(0);
                        long l = (long) d.doubleValue();
                        String timeStr = getTimeStringForMySqlDb(l);
                        valueStr = valueStr.append("'");
                        valueStr = valueStr.append(timeStr);
                        valueStr = valueStr.append("'");
                    } else {
                        valueStr = valueStr.append(cd.getData(0));
                    }
                    valueStr = valueStr.append(",");
                }
                valueStr = valueStr.delete(valueStr.length() - 1, valueStr.length());
                fieldNamesStr = fieldNamesStr.delete(fieldNamesStr.length() - 1, fieldNamesStr.length());
                fieldNamesStr = fieldNamesStr.append(")");
                valueStr = valueStr.append(")");
                Debugger.debug(Debugger.TRACE, "fieldNamesStr=" + fieldNamesStr);
                Debugger.debug(Debugger.TRACE, "valueStr=" + valueStr);
                insertIntoDb(smd, fieldNamesStr, valueStr);
            }
        }
        Debugger.debug(Debugger.TRACE, "===========================================");
    }
