    @Override
    @SuppressWarnings("unchecked")
    protected void createActions(GraphicalViewer viewer) {
        super.createActions(viewer);
        ActionRegistry registry = getActionRegistry();
        IAction action;
        action = new ShowStructuralChainsAction(this);
        registry.registerAction(action);
        action = new CreateDerivedRelationAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        action = new DeleteFromModelAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        for (IViewpoint viewPoint : ViewpointsManager.INSTANCE.getAllViewpoints()) {
            action = new ViewpointAction(this, viewPoint);
            registry.registerAction(action);
        }
    }
