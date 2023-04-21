    private void printServerNode(int level, String serverName) {
        printlnAtLevel(level, "[Printing list for Server = " + serverName + "]");
        String pattern = serverName + "/...";
        if (level == 0) pattern = "/" + pattern;
        printlnAtLevel(level, "[Pattern = " + pattern + "]");
        ChannelTree tr = getChannelTree(pattern);
        if (tr == null) {
            printlnAtLevel(level, "[no tree]");
            return;
        }
        Iterator i = tr.rootIterator();
        if (!i.hasNext()) {
            printlnAtLevel(level, "[empty root list]");
            return;
        }
        ChannelTree.Node node = (ChannelTree.Node) i.next();
        if (!node.getType().toString().equals("Server")) {
            printlnAtLevel(level, "[unexpected non-Server node type = " + node.getType() + "]");
            return;
        }
        if (!node.getName().equals(serverName)) {
            printlnAtLevel(level, "[unexpected node name = " + node.getName() + "]");
            return;
        }
        printlnAtLevel(level, node.getName() + " -- " + node.getFullName() + " (" + node.getType() + ")");
        printChildren(level + 1, node);
    }
