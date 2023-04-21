    public EditorToggleGridAction(GraphicalViewer diagramViewer) {
        setText(Messages.getString("org.isistan.flabot.edit.editor.actions.EditorToggleGridAction.text"));
        this.diagramViewer = diagramViewer;
        setToolTipText(Messages.getString("org.isistan.flabot.edit.editor.actions.EditorToggleGridAction.toolTipText"));
        setId(GEFActionConstants.TOGGLE_GRID_VISIBILITY);
        setImageDescriptor(ImageDescriptor.createFromFile(FlabotPlugin.class, "icons/grid.gif"));
        setChecked((Boolean) diagramViewer.getProperty(SnapToGrid.PROPERTY_GRID_ENABLED));
    }
