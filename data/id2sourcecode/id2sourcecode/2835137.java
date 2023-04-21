    @Override
    protected void initializeGraphicalViewer() {
        EditPartViewer viewer = getGraphicalViewer();
        viewer.setContents(new DiagramImpl());
    }
