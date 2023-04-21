    public EditorSnapGeometryAction(GraphicalViewer diagramViewer) {
        super(Messages.getString("org.isistan.flabot.edit.editor.actions.EditorSnapGeometryAction.text"), AS_CHECK_BOX);
        this.diagramViewer = diagramViewer;
        setToolTipText(Messages.getString("org.isistan.flabot.edit.editor.actions.EditorSnapGeometryAction.toolTipText"));
        setId(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY);
        setChecked((Boolean) diagramViewer.getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED));
    }
