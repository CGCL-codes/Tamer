    protected void createGraphicalViewer(Composite composite) {
        ERDScrollingGraphicalViewer erdscrollinggraphicalviewer = new ERDScrollingGraphicalViewer();
        erdscrollinggraphicalviewer.createControl(composite);
        setGraphicalViewer(erdscrollinggraphicalviewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }
