    @Override
    protected void createGraphicalViewer(Composite parent) {
        GEFGanttchartViewer viewer = new GEFGanttchartViewer();
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
