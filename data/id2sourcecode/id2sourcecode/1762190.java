    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new UCMEditPartFactory());
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, getModel().getSnapToGeometryEnabled());
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, getModel().getGridEnabled());
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, getModel().getGridEnabled());
        getGraphicalViewer().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                if (SnapToGrid.PROPERTY_GRID_VISIBLE.equals(event.getPropertyName())) getModel().setGridEnabled((Boolean) event.getNewValue());
                if (SnapToGeometry.PROPERTY_SNAP_ENABLED.equals(event.getPropertyName())) getModel().setSnapToGeometryEnabled((Boolean) event.getNewValue());
            }
        });
        ContextMenuProvider cmProvider = new UCMEditorContextMenuProvider(viewer, getActionRegistry(), this);
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
        IAction action = new ExportDiagramAction(getGraphicalViewer(), this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        action = new PrintDiagramAction(getGraphicalViewer(), this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        action = new EditorSnapGeometryAction(viewer);
        getActionRegistry().registerAction(action);
        action = new EditorToggleGridAction(viewer);
        getActionRegistry().registerAction(action);
    }
