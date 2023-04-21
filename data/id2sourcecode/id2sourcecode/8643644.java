		 * For example, GEF includes zoomable and scrollable root edit parts. The EditPartFactory
		 * maps model elements to edit parts (controllers).</p>
		 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
		 */
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new ComponentEditPartFactory());
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        ContextMenuProvider cmProvider = new ComponentEditorContextMenuProvider(viewer, getActionRegistry(), this);
        viewer.setContextMenu(cmProvider);
        getSite().registerContextMenu(cmProvider, viewer);
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, getModel().getSnapToGeometryEnabled());
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, getModel().getGridEnabled());
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, getModel().getGridEnabled());
        getGraphicalViewer().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                if (SnapToGrid.PROPERTY_GRID_VISIBLE.equals(event.getPropertyName())) getModel().setGridEnabled((Boolean) event.getNewValue());
                if (SnapToGeometry.PROPERTY_SNAP_ENABLED.equals(event.getPropertyName())) getModel().setSnapToGeometryEnabled((Boolean) event.getNewValue());
            }
        });
        IAction action = new ExportDiagramAction(getGraphicalViewer(), this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        action = new PrintDiagramAction(getGraphicalViewer(), this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        action = new EditorSnapGeometryAction(viewer);
