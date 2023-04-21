    public ToggleSnapToGeometryAction(GraphicalViewer diagramViewer) {
        super(GEFMessages.ToggleSnapToGeometry_Label, AS_CHECK_BOX);
        this.diagramViewer = diagramViewer;
        setToolTipText(GEFMessages.ToggleSnapToGeometry_Tooltip);
        setId(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY);
        setActionDefinitionId(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY);
        setChecked(isChecked());
    }
