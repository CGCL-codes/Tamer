    @Override
    protected String doCopy() throws Exception {
        String result = super.doCopy();
        for (Object o : getEObjects()) {
            if (o instanceof Node && ((Node) o).eContainer() instanceof DiagramImpl) {
                DiagramImpl diag = (DiagramImpl) ((Node) o).eContainer();
                IEditorPart editor = AsteriskDiagramEditorPlugin.getInstance().getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
                if (editor instanceof AsteriskDiagramEditor) {
                    AsteriskDiagramEditor asteriskDiagramEditor = (AsteriskDiagramEditor) editor;
                    HandlerEditPart part = (HandlerEditPart) asteriskDiagramEditor.getDiagramGraphicalViewer().getContents();
                    asteriskDiagramEditor.getDiagramGraphicalViewer().select(part);
                    break;
                }
            }
        }
        return result;
    }
