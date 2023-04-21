    private ChannelMap getChannelMap() throws SAPIException {
        ChannelMap initMap = new ChannelMap();
        ChannelTree ctree = null;
        Sink rbnbSink = new Sink();
        ArrayList<String> childServers = new ArrayList<String>();
        initMap.Add("*");
        rbnbSink.OpenRBNBConnection(rbnbServer, "SRBsink");
        rbnbSink.RequestRegistration(initMap);
        rbnbSink.Fetch(-1, initMap);
        ctree = ChannelTree.createFromChannelMap(initMap, "*");
        Iterator treeIt = ctree.iterator();
        ChannelTree.Node node = null;
        while (treeIt.hasNext()) {
            node = (ChannelTree.Node) (treeIt.next());
            if (node.getType() == ChannelTree.SERVER) {
                childServers.add(node.getName());
            }
        }
        logger.info("Detected " + childServers.size() + " child rbnb server" + ((1 < childServers.size()) ? "s" : ""));
        Iterator childIterator = childServers.iterator();
        initMap.Clear();
        int childCnt = 0;
        while (childIterator.hasNext()) {
            StringBuffer sbuff = new StringBuffer();
            sbuff.append(childIterator.next());
            sbuff.append("/*/*");
            initMap.Add(sbuff.toString());
            childCnt++;
        }
        logger.info("Loaded " + childCnt + " child server name" + ((1 < childCnt) ? "s" : "") + " into cmap.");
        rbnbSink.RequestRegistration(initMap);
        rbnbSink.Fetch(-1, initMap);
        ChannelMap retval = validateCmapForData(initMap);
        rbnbSink.RequestRegistration(retval);
        rbnbSink.Fetch(-1, retval);
        double reqEnd = reqStart + reqDuration;
        logger.info("Got cmap entry count: " + retval.NumberOfChannels() + " start: " + ISOtoRbnbTime.formatDate((long) (reqStart * 1000)) + " end: " + ISOtoRbnbTime.formatDate((long) (reqEnd * 1000)));
        rbnbSink.Request(retval, 0, 0, "newest");
        rbnbSink.Fetch(1000, retval);
        rbnbSink.CloseRBNBConnection();
        return retval;
    }
