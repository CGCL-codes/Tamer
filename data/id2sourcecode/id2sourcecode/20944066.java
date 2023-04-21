    public ToggleSnapToGridAction(ERDesignerEditor erdesignereditor, GraphicalViewer graphicalviewer) {
        super(SNAPTOGRID, 2);
        diagramViewer = graphicalviewer;
        setToolTipText(SNAPTOGRID);
        setId(ID);
        setActionDefinitionId(ID);
        setChecked(isChecked());
        editor = erdesignereditor;
    }
