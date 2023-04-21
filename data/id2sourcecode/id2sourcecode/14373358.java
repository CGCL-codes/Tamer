    public PrintDiagramAction(GraphicalViewer viewer, IEditorPart editorPart) {
        super(editorPart);
        this.viewer = viewer;
        this.editorPart = editorPart;
        setText(Messages.getString("org.isistan.flabot.edit.editor.actions.PrintDiagramAction.text"));
        setToolTipText(Messages.getString("org.isistan.flabot.edit.editor.actions.PrintDiagramAction.toolTipText"));
        setId(ActionFactory.PRINT.getId());
        setEnabled(false);
    }
