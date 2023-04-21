    private void exec() {
        connect();
        ChannelTree t = getChannelTree("/...");
        Iterator i = t.rootIterator();
        int level = 0;
        if (!i.hasNext()) {
            printlnAtLevel(level, "[empty root list]");
        }
        ChannelTree.Node node = (ChannelTree.Node) i.next();
        if (!node.getType().toString().equals("Server")) {
            printlnAtLevel(level, "[unexpected non-Server node type = " + node.getType() + "]");
            return;
        }
        String serverName = node.getName();
        printServerNode(level, serverName);
    }
