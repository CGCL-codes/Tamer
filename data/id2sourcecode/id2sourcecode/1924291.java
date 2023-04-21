    @Override
    protected void configureGraphicalViewer() {
        double[] zoomLevels;
        ArrayList<String> zoomContributions;
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new EditPartFactoryImpl());
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        manager = rootEditPart.getZoomManager();
        getActionRegistry().registerAction(new ZoomInAction(manager));
        getActionRegistry().registerAction(new ZoomOutAction(manager));
        zoomLevels = new double[] { 0.1, 0.15, 0.25, 0.5, 0.75, 1.0, 1.5, 2.0 };
        manager.setZoomLevels(zoomLevels);
        zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);
        viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), MouseWheelZoomHandler.SINGLETON);
        ContextMenuProvider provider = new GraphEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
    }
