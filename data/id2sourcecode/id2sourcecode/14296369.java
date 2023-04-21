    public void reloadServerList() {
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode("Server List");
        for (ServerWithWnd server : serverList) {
            DefaultMutableTreeNode serverNode = new DefaultMutableTreeNode(server.server.getServer());
            final String[] channels = server.server.getChannels();
            for (String c : channels) {
                serverNode.add(new DefaultMutableTreeNode(c));
            }
            parentNode.add(serverNode);
        }
        serverTree.setModel(new DefaultTreeModel(parentNode));
    }
