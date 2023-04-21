    private void exec() {
        connect();
        if (!connected) return;
        ChannelTree t = getChannelTree("/...");
        Iterator i = t.rootIterator();
        int level = 0;
        if (!i.hasNext()) {
            printlnAtLevel(level, "[empty root list]");
            System.exit(1);
        }
        ChannelTree.Node node = (ChannelTree.Node) i.next();
        if (!node.getType().toString().equals("Server")) {
            printlnAtLevel(level, "[unexpected non-Server node type = " + node.getType() + "]");
            System.exit(1);
        }
        String serverName = node.getName();
        printServerNode(level, serverName);
    }
