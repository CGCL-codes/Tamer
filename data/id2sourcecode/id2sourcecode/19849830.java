    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        final GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new View2DPartFactory());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        ContextMenuProvider cmProvider = new View2DContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
    }
