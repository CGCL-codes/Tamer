    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new ShapesEditPartFactory());
        ScalableFreeformRootEditPart rootPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootPart);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
        ZoomManager zoomManager = rootPart.getZoomManager();
        double[] levels = new double[] { 0.1, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 2.0, 3.0, 5.0, 10.0, 20.0 };
        zoomManager.setZoomLevels(levels);
        List<String> list = new ArrayList<String>();
        list.add(FreeNoteConstants.ZOOM_SEPARATOR);
        list.add(ZoomManager.FIT_ALL);
        list.add(ZoomManager.FIT_HEIGHT);
        list.add(ZoomManager.FIT_WIDTH);
        zoomManager.setZoomLevelContributions(list);
        IAction action = new ZoomInAction(zoomManager);
        getActionRegistry().registerAction(action);
        action = new ZoomOutAction(zoomManager);
        getActionRegistry().registerAction(action);
        ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
    }
