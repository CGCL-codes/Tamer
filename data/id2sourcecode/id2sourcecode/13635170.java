    public ToggleGridAction(GraphicalViewer diagramViewer) {
        super(GEFMessages.ToggleGrid_Label, AS_CHECK_BOX);
        this.diagramViewer = diagramViewer;
        setToolTipText(GEFMessages.ToggleGrid_Tooltip);
        setId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
        setActionDefinitionId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
        setChecked(isChecked());
    }
