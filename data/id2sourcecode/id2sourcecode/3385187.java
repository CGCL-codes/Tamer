    protected void createGraphicalViewer(Composite parent) {
        GraphicalViewer viewer = new GraphicalTextViewer();
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
