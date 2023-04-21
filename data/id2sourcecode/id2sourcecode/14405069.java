    public DefaultMutableTreeNode getNodeByPathOld(DefaultMutableTreeNode root, TVChannelsSet.Channel channel) {
        String[] pathElements = RE_SPLIT_PATH.split(channel.getChannelID());
        DefaultMutableTreeNode currentNode = root;
        for (int i = 0; i < pathElements.length; i++) {
            DefaultMutableTreeNode nextCurrentNode = null;
            Enumeration childs = currentNode.children();
            while (childs.hasMoreElements()) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) childs.nextElement();
                if (pathElements[i].equals(child.getUserObject())) {
                    nextCurrentNode = child;
                    break;
                }
            }
            if (nextCurrentNode == null) {
                nextCurrentNode = new DefaultMutableTreeNode(channel);
                currentNode.add(nextCurrentNode);
            }
            currentNode = nextCurrentNode;
        }
        return currentNode;
    }
