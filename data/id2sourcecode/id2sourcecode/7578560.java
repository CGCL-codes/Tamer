    @Override
    protected void initializeGraphicalViewer() {
        final GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getModel());
    }
