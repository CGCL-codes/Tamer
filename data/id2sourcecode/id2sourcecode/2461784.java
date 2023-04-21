    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        ReportRootEditPart root = new ReportRootEditPart();
        viewer.setRootEditPart(root);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setEditPartFactory(getEditPartFactory());
        createRulers();
    }
