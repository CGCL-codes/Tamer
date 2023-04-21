    protected void createGraphicalViewer(Composite parent) {
        rulerComp = new RulerComposite(parent, SWT.NONE);
        super.createGraphicalViewer(rulerComp);
        rulerComp.setGraphicalViewer((ScrollingGraphicalViewer) getGraphicalViewer());
    }
