    public Map getTimeLimits(Map inMap) {
        if (!connected) return inMap;
        try {
            ChannelMap cm = new ChannelMap();
            Channel[] chan = inMap.channelList();
            for (int i = 0; i < chan.length; i++) {
                cm.Add("/" + chan[i].getChannelName());
                chan[i].clear();
            }
            sink.RequestRegistration(cm);
            ChannelMap regMap = sink.Fetch(2000);
            ChannelMap cm2 = null;
            for (int i = 0; i < regMap.NumberOfChannels(); i++) {
                if (regMap.GetTimeDuration(i) == 0) {
                    if (cm2 == null) cm2 = new ChannelMap();
                    cm2.Add(regMap.GetName(i));
                } else {
                    double[] times = new double[2];
                    times[0] = regMap.GetTimeStart(i);
                    times[1] = times[0] + regMap.GetTimeDuration(i);
                    DataTimeStamps dt = new DataTimeStamps(times);
                    Channel ch = inMap.findChannel(regMap.GetName(i).substring(1));
                    if (ch != null) ch.setTimeStamp(dt);
                }
            }
            if (cm2 != null) {
                sink.Request(cm2, 0, 0, "newest");
                ChannelMap newmap = sink.Fetch(2000);
                sink.Request(cm2, 0, 0, "oldest");
                ChannelMap oldmap = sink.Fetch(2000);
                for (int i = 0; i < oldmap.NumberOfChannels(); i++) {
                    Channel ch = inMap.findChannel(oldmap.GetName(i).substring(1));
                    double[] times = new double[2];
                    times[0] = oldmap.GetTimeStart(i);
                    int chnum = newmap.GetIndex(oldmap.GetName(i));
                    if (chnum >= 0) {
                        times[1] = newmap.GetTimeStart(chnum) + newmap.GetTimeDuration(chnum);
                        if (ch != null) ch.setTimeStamp(new DataTimeStamps(times));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Exception in Sink.getTimeLimits");
            e.printStackTrace();
        }
        return inMap;
    }
