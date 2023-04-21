    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new PlanEditPartFactory());
        viewer.setContextMenu(new PlanningContextMenuProvider(getGraphicalViewer(), getActionRegistry()));
    }
