    private void selectNode(String nodeName) {
        ChannelTree.Node node = treeModel.getChannelTree().findNode(nodeName);
        if (node != null) {
            int depth = node.getDepth();
            Object[] path = new Object[depth + 2];
            path[0] = treeModel.getRoot();
            for (int i = path.length - 1; i > 0; i--) {
                path[i] = node;
                node = node.getParent();
            }
            tree.addSelectionPath(new TreePath(path));
        }
    }
