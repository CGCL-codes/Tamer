    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        getGraphicalViewer().setRootEditPart(new ScalableRootEditPart());
        getGraphicalViewer().setEditPartFactory(new PartFactory());
        MenuManager provider = new ContextMenuProvider(getGraphicalViewer()) {

            @Override
            public void buildContextMenu(IMenuManager menu) {
                GEFActionConstants.addStandardActionGroups(menu);
                IAction action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
                if (action.isEnabled()) menu.appendToGroup(GEFActionConstants.GROUP_REST, action);
                action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
                menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
                action = getActionRegistry().getAction(ActionFactory.REDO.getId());
                menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);
                action = getActionRegistry().getAction(EditFlowVarsAction.EDIT_FLOW_VAR_ACTION_ID);
                if (action.isEnabled()) menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
                action = getActionRegistry().getAction(EditInputVarAction.EDIT_INPUT_VAR_ACTION_ID);
                if (action.isEnabled()) menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
            }
        };
        getGraphicalViewer().setContextMenu(provider);
    }
