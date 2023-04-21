    @SuppressWarnings({ "deprecation", "deprecation" })
    protected void configureGraphicalViewer() {
        logger.debug("Configuration du graphical viewer.");
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
        List<String> zoomLevels = new ArrayList<String>(3);
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        root.getZoomManager().setZoomLevelContributions(zoomLevels);
        IAction zoomIn = new ZoomInAction(root.getZoomManager());
        IAction zoomOut = new ZoomOutAction(root.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        getSite().getKeyBindingService().registerAction(zoomIn);
        getSite().getKeyBindingService().registerAction(zoomOut);
        viewer.setRootEditPart(root);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
        viewer.addDropTargetListener(new DataEditDropTargetListener(viewer));
        viewer.setEditPartFactory(new StatesMachinesEditPartFactory());
        viewer.setContents(getStatesMachines());
        getEditDomain().addViewer(getGraphicalViewer());
    }
