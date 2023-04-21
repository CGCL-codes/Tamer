    /**
		 * Set up the editor's inital content (after creation).
		 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#initializeGraphicalViewer()
		 */
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getModel());
        ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) viewer.getRootEditPart();
        ConnectionLayer connLayer = (ConnectionLayer) root.getLayer(LayerConstants.CONNECTION_LAYER);
        GraphicalEditPart contentEditPart = (GraphicalEditPart) root.getContents();
