    protected void createGraphicalViewer(Composite parent) {
        rulerComposite = new ReportRulerComposite(parent, SWT.NONE);
        GraphicalViewer viewer = new J2DScrollingGraphicalViewer();
        viewer.createControl(rulerComposite);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        parent.layout();
        initializeGraphicalViewer();
        FigureCanvas editor = (FigureCanvas) viewer.getControl();
        ((DefaultRangeModel) editor.getViewport().getHorizontalRangeModel()).setMinimum(30);
        rulerComposite.setGraphicalViewer((ScrollingGraphicalViewer) viewer);
    }
