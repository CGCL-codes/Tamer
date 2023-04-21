    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
    }
