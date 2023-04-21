    @Override
    protected void registerContextMenu(GraphicalViewer viewer) {
        MenuManager provider = new ArchimateDiagramEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu(ArchimateDiagramEditorContextMenuProvider.ID, provider, viewer);
    }
