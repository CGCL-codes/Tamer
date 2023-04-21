    public ToggleSnapToGridAction(ReportEditor erdesignereditor, GraphicalViewer graphicalviewer) {
        super(SNAPTOGRID, 2, erdesignereditor, graphicalviewer);
        setToolTipText(SNAPTOGRID);
        setId(ID);
        setActionDefinitionId(ID);
        setChecked(isChecked());
    }
