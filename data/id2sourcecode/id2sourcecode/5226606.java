    private void printServerNode(int level, String serverName) {
        String pattern = serverName + "/...";
        if (level == 0) pattern = "/" + pattern;
        ChannelTree tr = getChannelTree(pattern);
        if (tr == null) {
            printlnAtLevel(level, "[no tree]");
            System.exit(1);
        }
        Iterator i = tr.rootIterator();
        if (!i.hasNext()) {
            printlnAtLevel(level, "[empty root list]");
            System.exit(1);
        }
        ChannelTree.Node node = (ChannelTree.Node) i.next();
        if (!node.getType().toString().equals("Server")) {
            printlnAtLevel(level, "[unexpected non-Server node type = " + node.getType() + "]");
            System.exit(1);
        }
        if (!node.getName().equals(serverName)) {
            printlnAtLevel(level, "[unexpected node name = " + node.getName() + "]");
            System.exit(1);
        }
        boolean process = ((!node.getName().startsWith("_")) || includeHidden) || (node.getType().toString().equals("Source") && sourceOnly);
        if (process) {
            if (node.getType().toString().equals("Channel")) printlnAtLevel(level, node.getFullName());
            printChildren(level + 1, node);
        }
    }
