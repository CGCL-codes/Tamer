    public void testGetRootEditPart() {
        Util.activateLogicEditor();
        LogicEditor editor = (LogicEditor) Util.getLogicEditor();
        RootEditPart root = GEFWorkbenchUtilities.getRootEditPart(editor);
        GraphicalViewer viewer = (GraphicalViewer) root.getViewer();
        DefaultEditDomain domain = (DefaultEditDomain) viewer.getEditDomain();
        assertSame(editor, domain.getEditorPart());
    }
