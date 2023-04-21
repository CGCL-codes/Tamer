    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        WorkflowRootEditPart root = new WorkflowRootEditPart();
        getGraphicalViewer().setRootEditPart(root);
        List<String> zoomLevels = new ArrayList<String>(3);
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        root.getZoomManager().setZoomLevelContributions(zoomLevels);
        getGraphicalViewer().setEditPartFactory(new PartFactory());
        getCommandStack().addCommandStackListener(listener);
    }
