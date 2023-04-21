    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
        getGraphicalViewer().setRootEditPart(rootEditPart);
        DashboardEditPartFactory dashboardEditPartFactory = new DashboardEditPartFactory();
        dashboardEditPartFactory.setProject(getProject());
        getGraphicalViewer().setEditPartFactory(dashboardEditPartFactory);
        ContextMenuProvider provider = new DashboardContextMenuProvider(getGraphicalViewer(), getActionRegistry());
        getGraphicalViewer().setContextMenu(provider);
        getEditorSite().registerContextMenu("net.entropysoft.dashboard.plugin.editor.contextmenu", provider, getGraphicalViewer(), false);
        ZoomManager zoomManager = rootEditPart.getZoomManager();
        List<String> zoomContributions = Arrays.asList(new String[] { ZoomManager.FIT_ALL, ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH });
        zoomManager.setZoomLevelContributions(zoomContributions);
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
        IAction zoomIn = new ZoomInAction(zoomManager);
        IAction zoomOut = new ZoomOutAction(zoomManager);
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);
        getSite().getKeyBindingService().registerAction(zoomIn);
        getSite().getKeyBindingService().registerAction(zoomOut);
        IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
        getActionRegistry().registerAction(showRulers);
        IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
        getActionRegistry().registerAction(snapAction);
        IAction showGrid = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(showGrid);
    }
