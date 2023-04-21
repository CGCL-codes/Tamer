    private void configureContextMenu(GraphicalViewer viewer) {
        MenuManager menuManager = new QueryContextMenuManager(getActionRegistry());
        viewer.setContextMenu(menuManager);
    }
