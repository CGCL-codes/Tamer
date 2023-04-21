    private void configureContextMenu(GraphicalViewer viewer) {
        MenuManager menuManager = new ClassDiagramContextMenuManager(getActionRegistry());
        viewer.setContextMenu(menuManager);
    }
