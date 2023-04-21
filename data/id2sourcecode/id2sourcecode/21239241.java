    private ChannelTree getChannelTree(String pattern) {
        ChannelTree tr = null;
        try {
            sMap = new ChannelMap();
            if ((pattern != null) && (pattern.length() > 0)) sMap.Add(pattern);
            sink.RequestRegistration(sMap);
            sMap = sink.Fetch(-1, sMap);
            tr = ChannelTree.createFromChannelMap(sMap);
        } catch (SAPIException se) {
            se.printStackTrace();
        }
        return tr;
    }
