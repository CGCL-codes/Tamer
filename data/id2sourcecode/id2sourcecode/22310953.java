    public String[][] getChannelList(String match) {
        try {
            ChannelMap cm = new ChannelMap();
            cm.Add(sink.GetServerName() + "/*/...");
            sink.RequestRegistration(cm);
            cm = sink.Fetch(-1);
            String[][] chans = new String[3][];
            chans[0] = cm.GetChannelList();
            chans[1] = new String[chans[0].length];
            for (int i = 0; i < chans[0].length; i++) {
                chans[1][i] = cm.GetUserInfo(i);
            }
            chans[2] = new String[chans[0].length];
            for (int i = 0; i < chans[0].length; i++) {
                if (cm.GetType(i) == ChannelMap.TYPE_STRING) {
                    String xmldat = cm.GetDataAsString(i)[0];
                    if (xmldat != null && xmldat.length() > 0) {
                        int i1 = xmldat.indexOf("<mime>");
                        int i2 = xmldat.indexOf("</mime>");
                        if (i1 > -1 && i2 > -1) chans[2][i] = xmldat.substring(i1 + 6, i2);
                    }
                }
            }
            return chans;
        } catch (SAPIException se) {
            se.printStackTrace();
            return null;
        }
    }
