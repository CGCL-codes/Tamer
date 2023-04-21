    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setEditPartFactory(new DetailedEditPartFactory(new DetailedFigureFactory()));
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
    }
