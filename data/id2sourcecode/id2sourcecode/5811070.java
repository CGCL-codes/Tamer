    public void testGetViewer() {
        Util.activateLogicEditor();
        LogicEditor editor = (LogicEditor) Util.getLogicEditor();
        GraphicalViewer viewer = GEFWorkbenchUtilities.getViewer(editor);
        DefaultEditDomain domain = (DefaultEditDomain) viewer.getEditDomain();
        assertSame(editor, domain.getEditorPart());
    }
