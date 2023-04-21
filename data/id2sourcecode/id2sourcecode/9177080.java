    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GEFDeleteAction deleteAction = new GEFDeleteAction(this);
        getActionRegistry().registerAction(deleteAction);
        getSelectionActions().add(deleteAction.getId());
        SelectionAction action = new GEFCopyAction(this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        action = new GEFPasteAction(this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        for (EConnectionType type : EConnectionType.values()) {
            ConnectionCreateAction createAction = new ConnectionCreateAction(this, type);
            getActionRegistry().registerAction(createAction);
            getSelectionActions().add(createAction.getId());
        }
        ContextMenuProvider menuProvider = new GEFEditorContextMenuProvider(this, viewer, getActionRegistry());
        viewer.setContextMenu(menuProvider);
        getGraphicalViewer().setEditPartFactory(new CompProcessPartFactory());
    }
