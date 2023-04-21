    public ExportAsImageToClipboardAction(GraphicalViewer diagramViewer) {
        super(TEXT);
        fDiagramViewer = diagramViewer;
        setId(ID);
        setActionDefinitionId(getId());
    }
