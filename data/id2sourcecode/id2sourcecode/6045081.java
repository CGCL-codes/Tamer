    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
        List zoomLevels = new ArrayList(3);
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
        viewer.setEditPartFactory(new GraphicalPartFactory());
        ContextMenuProvider provider = new LogicContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu("org.eclipse.gef.examples.logic.editor.contextmenu", provider, viewer);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
        loadProperties();
        IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
        getActionRegistry().registerAction(showRulers);
        IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
        getActionRegistry().registerAction(snapAction);
        IAction showGrid = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(showGrid);
        Listener listener = new Listener() {

            public void handleEvent(Event event) {
                handleActivationChanged(event);
            }
        };
        getGraphicalControl().addListener(SWT.Activate, listener);
        getGraphicalControl().addListener(SWT.Deactivate, listener);
    }
