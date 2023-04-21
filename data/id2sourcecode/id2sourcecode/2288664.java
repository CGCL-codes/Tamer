    protected void createGraphicalViewer(Composite parent) {
        GraphicalViewer viewer = new J2DScrollingGraphicalViewer();
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
