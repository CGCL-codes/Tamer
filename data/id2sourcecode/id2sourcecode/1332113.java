    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        org.codescale.eDependency.diagram.part.DiagramEditorContextMenuProvider provider = new org.codescale.eDependency.diagram.part.DiagramEditorContextMenuProvider(this, getDiagramGraphicalViewer());
        getDiagramGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, getDiagramGraphicalViewer());
    }
