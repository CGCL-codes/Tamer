    protected void createGraphicalViewer(Composite parent) {
        System.out.println("Creating graphical viewer!!1");
        System.out.flush();
        GraphicalViewer viewer = new J2DScrollingGraphicalViewer();
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
