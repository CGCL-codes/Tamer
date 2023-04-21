    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        de.nordakademie.lejos.stateMachine.diagram.part.DiagramEditorContextMenuProvider provider = new de.nordakademie.lejos.stateMachine.diagram.part.DiagramEditorContextMenuProvider(this, getDiagramGraphicalViewer());
        getDiagramGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, getDiagramGraphicalViewer());
    }
