    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        setViewer(getGraphicalViewer());
        getViewer().setEditPartFactory(new ProcessViewControllerFactory());
        rootEditPart = new ScalableFreeformRootEditPart();
        getViewer().setRootEditPart(rootEditPart);
        getViewer().setKeyHandler(new GraphicalViewerKeyHandler(getViewer()));
    }
