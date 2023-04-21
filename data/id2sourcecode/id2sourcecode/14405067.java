    public DefaultMutableTreeNode getTreeByChannelsSet() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(null);
        Iterator it = channels.allChannels.getChannels().iterator();
        while (it.hasNext()) {
            TVChannelsSet.Channel ch = (TVChannelsSet.Channel) it.next();
            DefaultMutableTreeNode node = getNodeByPath(root, ch.getChannelID());
            node.setUserObject(ch);
        }
        return root;
    }
