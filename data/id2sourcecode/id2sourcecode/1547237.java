    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        viewer.setEditPartFactory(new DBEditPartFactory());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        GEFDeleteAction deleteAction = new GEFDeleteAction(this);
        getActionRegistry().registerAction(deleteAction);
        getSelectionActions().add(deleteAction.getId());
        SelectionAction action = new GEFCopyAction(this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        action = new GEFCutAction(this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        action = new GEFPasteAction(this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        ContextMenuProvider menuProvider = new GEFEditorContextMenuProvider(this, viewer, getActionRegistry());
        viewer.setContextMenu(menuProvider);
        getGraphicalViewer().setKeyHandler(new GraphicalViewerKeyHandler(getGraphicalViewer()).setParent(getCommonKeyHandler()));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(GRID_SIZE, GRID_SIZE));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, new Boolean(true));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, new Boolean(true));
    }
