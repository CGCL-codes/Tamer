    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        edu.toronto.cs.openome_model.diagram.part.DiagramEditorContextMenuProvider provider = new edu.toronto.cs.openome_model.diagram.part.DiagramEditorContextMenuProvider(this, getDiagramGraphicalViewer());
        getDiagramGraphicalViewer().setContextMenu(provider);
    }
