    @Override
    protected void configureGraphicalViewer() {
        ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
        IAction zoomIn = new ZoomInAction(root.getZoomManager());
        IAction zoomOut = new ZoomOutAction(root.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        List zoomLevels = new ArrayList(3);
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        root.getZoomManager().setZoomLevelContributions(zoomLevels);
        IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
        handlerService.activateHandler(zoomIn.getActionDefinitionId(), new ActionHandler(zoomIn));
        handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
        handlerService.activateHandler(zoomOut.getActionDefinitionId(), new ActionHandler(zoomOut));
        IAction snapAction = new ToggleSnapToGeometryAction(viewer) {

            @Override
            public void run() {
                super.run();
                if (isChecked()) {
                    currentGridResolution = AgentEditor.DEFAULT_GRID_RESOLUTION;
                } else {
                    currentGridResolution = 1;
                }
            }
        };
        snapAction.setChecked(true);
        getActionRegistry().registerAction(snapAction);
        viewer.setRootEditPart(root);
        viewer.setEditPartFactory(new AgentBuilderEditPartFactory(getFlowFile()));
        ContextMenuProvider provider = new AgentBuilderContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu("repast.simphony.agents.designer.ui.editors.contextmenu", provider, viewer);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
    }
