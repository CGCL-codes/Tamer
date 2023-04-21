    public Map getMap(Map map, Time startT, Time durationT, int flags) {
        ChannelMap cm = null;
        int retVal = 0;
        int numChan = 0;
        String timeRef = new String("absolute");
        double start = 0;
        double duration = 0;
        if (startT != null) start = startT.getDoubleValue();
        if (durationT != null) duration = durationT.getDoubleValue();
        cm = new ChannelMap();
        Channel[] chan = map.channelList();
        for (int i = 0; i < chan.length; i++) {
            try {
                cm.Add("/" + chan[i].getChannelName());
                chan[i].clear();
            } catch (Exception e) {
                System.err.println("com.rbnb.plot.Sink.getMap: exception ");
                e.printStackTrace();
            }
        }
        if ((flags & DataRequest.newest) == DataRequest.newest) {
            timeRef = new String("newest");
            start = 0;
        } else if ((flags & DataRequest.oldest) == DataRequest.oldest) {
            timeRef = new String("oldest");
            start = 0;
        }
        try {
            sink.Request(cm, start, duration, timeRef);
            cm = sink.Fetch(60000);
            lastMap = cm;
            if (cm.GetIfFetchTimedOut()) System.err.println("***********rbnbPlot fetch timed out*************");
            numChan = cm.NumberOfChannels();
        } catch (SAPIException se) {
            se.printStackTrace();
        }
        for (int i = 0; i <= numChan - 1; i++) {
            boolean getTimeStamp = true;
            Channel ch = map.findChannel(cm.GetName(i));
            if (ch != null) {
                switch(cm.GetType(i)) {
                    case ChannelMap.TYPE_BYTEARRAY:
                        ch.setDataByteArray(cm.GetDataAsByteArray(i), cm.GetMime(i));
                        break;
                    case ChannelMap.TYPE_STRING:
                        ch.setDataString(cm.GetDataAsString(i), cm.GetMime(i));
                        break;
                    case ChannelMap.TYPE_INT8:
                        ch.setDataInt8(cm.GetDataAsInt8(i));
                        break;
                    case ChannelMap.TYPE_INT16:
                        ch.setDataInt16(cm.GetDataAsInt16(i));
                        break;
                    case ChannelMap.TYPE_INT32:
                        ch.setDataInt32(cm.GetDataAsInt32(i));
                        break;
                    case ChannelMap.TYPE_INT64:
                        ch.setDataInt64(cm.GetDataAsInt64(i));
                        break;
                    case ChannelMap.TYPE_FLOAT32:
                        ch.setDataFloat32(cm.GetDataAsFloat32(i));
                        break;
                    case ChannelMap.TYPE_FLOAT64:
                        ch.setDataFloat64(cm.GetDataAsFloat64(i));
                        break;
                    default:
                        for (int j = 0; j < chan.length; j++) {
                            chan[j].clear();
                        }
                        getTimeStamp = false;
                        break;
                }
                if (getTimeStamp) {
                    DataTimeStamps ts = new DataTimeStamps(cm.GetTimes(i));
                    ch.setTimeStamp(ts);
                }
            }
        }
        return map;
    }
