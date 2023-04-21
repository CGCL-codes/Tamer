    private GraphicalViewer configureViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new GraphicalPartFactory());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        return viewer;
    }
