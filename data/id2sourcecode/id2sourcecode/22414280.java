    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        de.hu_berlin.sam.mmunit.diagram.part.DiagramEditorContextMenuProvider provider = new de.hu_berlin.sam.mmunit.diagram.part.DiagramEditorContextMenuProvider(this, getDiagramGraphicalViewer());
        getDiagramGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, getDiagramGraphicalViewer());
    }
