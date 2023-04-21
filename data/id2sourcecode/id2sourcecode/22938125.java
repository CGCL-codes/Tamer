    @Override
    protected void initializeGraphicalViewer() {
        viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        root = new EmptyRootEditPart();
        viewer.setRootEditPart(root);
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(getCommonKeyHandler()));
    }
