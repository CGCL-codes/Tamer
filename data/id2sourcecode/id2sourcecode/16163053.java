    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        ScalableFreeformRootEditPart rootPart = new ScalableFreeformRootEditPart();
        viewer.setEditPartFactory(new CsdeEditPartFactory());
        rootPart.getZoomManager().setZoomLevels(new double[] { .1, .2, .3, .4, .5, .6, .7, .8, .9, 1.0 });
        List zoomLevels = new ArrayList(3);
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        rootPart.getZoomManager().setZoomLevelContributions(zoomLevels);
        IAction zoomIn = new ZoomInAction(rootPart.getZoomManager());
        IAction zoomOut = new ZoomOutAction(rootPart.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        getSite().getKeyBindingService().registerAction(zoomIn);
        getSite().getKeyBindingService().registerAction(zoomOut);
        viewer.setRootEditPart(rootPart);
        ContextMenuProvider cmProvider = new CsdeContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
        viewer.setKeyHandler(getCommonKeyHandler());
        getSite().registerContextMenu(cmProvider, viewer);
    }
