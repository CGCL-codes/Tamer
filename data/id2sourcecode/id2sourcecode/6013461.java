    public void update() {
        _favoritesNode.update();
        try {
            ((DefaultTreeModel) getModel()).reload();
        } catch (Exception e) {
        }
        expandPath(new TreePath(_favoritesNode.getServersNode().getPath()));
        expandPath(new TreePath(_favoritesNode.getChannelsNode().getPath()));
        expandPath(new TreePath(_favoritesNode.getUsersNode().getPath()));
        setRowHeight(FavoritesPanel.ROW_HEIGHT);
    }
