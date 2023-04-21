    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setRootEditPart(new FreeformGraphicalRootEditPart());
        getGraphicalViewer().setEditPartFactory(new CFlowEditPartFactory(view));
    }
