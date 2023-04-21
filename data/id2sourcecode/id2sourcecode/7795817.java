    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        DashboardEditPartFactory dashboardEditPartFactory = new DashboardEditPartFactory();
        dashboardEditPartFactory.setProject(getProject());
        getGraphicalViewer().setEditPartFactory(dashboardEditPartFactory);
        ContextMenuProvider provider = new DashboardContextMenuProvider(getGraphicalViewer(), getActionRegistry());
        getGraphicalViewer().setContextMenu(provider);
        IAction showRulers = new ToggleRulerVisibilityAction(getGraphicalViewer());
        getActionRegistry().registerAction(showRulers);
        IAction snapAction = new ToggleSnapToGeometryAction(getGraphicalViewer());
        getActionRegistry().registerAction(snapAction);
        IAction showGrid = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(showGrid);
    }
