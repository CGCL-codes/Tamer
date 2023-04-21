    private ChannelTree getChannelTree(Sink sink, String path, Map<String, Channel> channels, int depth) throws SAPIException {
        depth++;
        ChannelTree ctree = ChannelTree.EMPTY_TREE;
        ChannelMap markerChannelMap = new ChannelMap();
        ChannelMap cmap = new ChannelMap();
        if (path == null) {
            path = "";
            cmap.Add("...");
        } else {
            cmap.Add(path + "/...");
        }
        sink.RequestRegistration(cmap);
        cmap = sink.Fetch(FETCH_TIMEOUT, cmap);
        if (cmap.GetIfFetchTimedOut()) {
            log.error("Failed to get metadata.  Fetch timed out.");
            return ctree;
        }
        ctree = ChannelTree.createFromChannelMap(cmap);
        String[] channelList = cmap.GetChannelList();
        for (int i = 0; i < channelList.length; i++) {
            int channelIndex = cmap.GetIndex(channelList[i]);
            if (channelIndex != -1) {
                ChannelTree.Node node = ctree.findNode(channelList[i]);
                String userMetadata = cmap.GetUserInfo(channelIndex);
                Channel channel = new Channel(node, userMetadata);
                channels.put(channelList[i], channel);
                String mimeType = channel.getMetadata("mime");
                if (mimeType != null && mimeType.compareToIgnoreCase(EventMarker.MIME_TYPE) == 0) {
                    markerChannelMap.Add(node.getFullName());
                }
            }
        }
        Iterator it = ctree.iterator();
        while (it.hasNext()) {
            ChannelTree.Node node = (ChannelTree.Node) it.next();
            NodeTypeEnum type = node.getType();
            if ((type == ChannelTree.SERVER || type == ChannelTree.PLUGIN) && !path.startsWith(node.getFullName()) && depth < MAX_REQUEST_DEPTH) {
                ChannelTree childChannelTree = getChannelTree(sink, node.getFullName(), channels, depth);
                ctree = childChannelTree.merge(ctree);
            }
        }
        if (markerChannelMap.NumberOfChannels() > 0) {
            double markersDuration = System.currentTimeMillis() / 1000d;
            sink.Request(markerChannelMap, 0, markersDuration, "absolute");
            markerChannelMap = sink.Fetch(FETCH_TIMEOUT, markerChannelMap);
            if (!markerChannelMap.GetIfFetchTimedOut()) {
                fireMarkersUpdated(markerChannelMap);
            } else {
                log.error("Failed to get event markers. Fetched timed out.");
            }
        }
        return ctree;
    }
