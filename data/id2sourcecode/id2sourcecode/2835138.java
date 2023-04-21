    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new JMinerEditPartFactory());
        viewer.setRootEditPart(new ScalableFreeformRootEditPart());
    }
