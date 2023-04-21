    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setEditPartFactory(new PilotEditPartFactory(new PilotFigureFactory()));
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
    }
