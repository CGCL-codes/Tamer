    public ExportDiagramAction(GraphicalViewer viewer, IEditorPart editorPart) {
        super(editorPart);
        this.viewer = viewer;
        this.editorPart = editorPart;
        setText(Messages.getString("org.isistan.flabot.edit.editor.actions.ExportDiagramAction.text"));
        setToolTipText(Messages.getString("org.isistan.flabot.edit.editor.actions.ExportDiagramAction.toolTipText"));
        setId(EXPORT_DIAGRAM);
        setImageDescriptor(ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/export.gif"));
        setEnabled(false);
    }
