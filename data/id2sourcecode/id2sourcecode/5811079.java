    public void testGetEditDomain() {
        Util.activateLogicEditor();
        LogicEditor editor = (LogicEditor) Util.getLogicEditor();
        EditDomain editDomain = GEFWorkbenchUtilities.getEditDomain(editor);
        GraphicalViewer viewer = (GraphicalViewer) editor.getAdapter(GraphicalViewer.class);
        assertSame(viewer.getEditDomain(), editDomain);
    }
