    public ToggleGridVisibilityAction(ERDesignerEditor erdesignereditor, GraphicalViewer graphicalviewer) {
        super(GEFMessages.ToggleGrid_Label, 2);
        diagramViewer = graphicalviewer;
        setToolTipText(GEFMessages.ToggleGrid_Tooltip);
        setId("org.eclipse.gef.toggle_grid_visibility");
        setActionDefinitionId("org.eclipse.gef.toggle_grid_visibility");
        setChecked(isChecked());
        editor = erdesignereditor;
    }
