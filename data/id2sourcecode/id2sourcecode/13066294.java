    @SuppressWarnings("unchecked")
    private void initViewerAction(GraphicalViewer viewer) {
        ScalableFreeformRootEditPart rootEditPart = new PagableFreeformRootEditPart(this.diagram);
        viewer.setRootEditPart(rootEditPart);
        ZoomManager manager = rootEditPart.getZoomManager();
        double[] zoomLevels = new double[] { 0.1, 0.25, 0.5, 0.75, 0.8, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
        manager.setZoomLevels(zoomLevels);
        List<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);
        ZoomInAction zoomInAction = new ZoomInAction(manager);
        ZoomOutAction zoomOutAction = new ZoomOutAction(manager);
        ZoomAdjustAction zoomAdjustAction = new ZoomAdjustAction(manager);
        this.getActionRegistry().registerAction(zoomInAction);
        this.getActionRegistry().registerAction(zoomOutAction);
        this.getActionRegistry().registerAction(zoomAdjustAction);
        this.addKeyHandler(zoomInAction);
        this.addKeyHandler(zoomOutAction);
        IFigure gridLayer = rootEditPart.getLayer(LayerConstants.GRID_LAYER);
        gridLayer.setForegroundColor(Resources.GRID_COLOR);
        IAction action = new ToggleGridAction(viewer);
        this.getActionRegistry().registerAction(action);
        action = new ChangeBackgroundColorAction(this, this.diagram);
        this.getActionRegistry().registerAction(action);
        this.getSelectionActions().add(action.getId());
        action = new TooltipAction(this);
        this.getActionRegistry().registerAction(action);
        action = new LockEditAction(this);
        this.getActionRegistry().registerAction(action);
        action = new ExportToDBAction(this);
        this.getActionRegistry().registerAction(action);
        this.actionBarContributor = new ERDiagramActionBarContributor(this.zoomComboContributionItem);
    }
