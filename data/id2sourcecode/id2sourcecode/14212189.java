    private void createGraphNodes(Kind k, ConsoleMagicSheet ms, JGraph graph, LiveCue c, int width, int widthGroup, int hightGroup) {
        GraphCell channelCell = null;
        int x = 0;
        int wSkip = 1;
        int hSkip = 1;
        while (x < console.getTotalChannels()) {
            if (k == Kind.CHANNEL) channelCell = createChannelVertex(x, x % width, x / width, wSkip, hSkip, cellWidth, cellHight, c); else if (k == Kind.MAGIC) {
                channelCell = createMagicVertex(x, 0, 0, cellWidth, cellHight, c);
                Rectangle2D bounds = GraphConstants.getBounds(channelCell.getAttributes());
                bounds.setRect(ms.getChannel(x).getX(), ms.getChannel(x).getY(), bounds.getWidth(), bounds.getHeight());
                Map transportMap = new Hashtable();
                GraphConstants.setBounds(transportMap, bounds);
                channelCell.changeAttributes(transportMap);
            }
            graph.getGraphLayoutCache().insert(channelCell);
            if ((x + 1) % width == 0) {
                if ((x + 1) / width % hightGroup == 0) hSkip++;
                wSkip = 1;
            } else if ((x + 1) % widthGroup == 0) wSkip++;
            x++;
        }
    }
