    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new ShapesEditPartFactory());
        viewer.setRootEditPart(new ShapesOnTopRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        ContextMenuProvider cmProvider = new EEditContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
        openPropertyView();
    }
