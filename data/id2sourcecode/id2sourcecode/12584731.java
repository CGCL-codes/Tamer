    @Override
    protected void registerContextMenu(GraphicalViewer viewer) {
        MenuManager provider = new CanvasEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(CanvasEditorContextMenuProvider.ID, provider, viewer);
    }
