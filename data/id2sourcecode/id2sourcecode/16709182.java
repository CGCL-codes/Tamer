    private void handlePopup(MouseEvent e) {
        TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
        if (treePath == null) {
            return;
        }
        if (!tree.isPathSelected(treePath)) {
            tree.setSelectionPath(treePath);
        }
        JPopupMenu popup = null;
        Object o = treePath.getLastPathComponent();
        if (o == treeModel.getRoot()) {
            popup = getRootPopup();
        } else {
            popup = getChannelPopup();
        }
        if (popup != null && popup.getComponentCount() > 0) {
            popup.show(tree, e.getX(), e.getY());
        }
    }
