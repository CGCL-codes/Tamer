    public ToggleRulerVisibilityAction(GraphicalViewer diagramViewer) {
        super(GEFMessages.ToggleRulerVisibility_Label, AS_CHECK_BOX);
        this.diagramViewer = diagramViewer;
        setToolTipText(GEFMessages.ToggleRulerVisibility_Tooltip);
        setId(GEFActionConstants.TOGGLE_RULER_VISIBILITY);
        setActionDefinitionId(GEFActionConstants.TOGGLE_RULER_VISIBILITY);
        setChecked(isChecked());
    }
