    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        SensorDataWebGui.diagram.part.DiagramEditorContextMenuProvider provider = new SensorDataWebGui.diagram.part.DiagramEditorContextMenuProvider(this, getDiagramGraphicalViewer());
        getDiagramGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, getDiagramGraphicalViewer());
    }
