    public void testGetPaletteRootFigure() {
        Util.activateLogicEditor();
        LogicEditor editor = (LogicEditor) Util.getLogicEditor();
        IFigure rootFigure = GEFWorkbenchUtilities.getPaletteRootFigure(editor);
        GraphicalViewer viewer = (GraphicalViewer) editor.getAdapter(GraphicalViewer.class);
        Map map = viewer.getEditDomain().getPaletteViewer().getVisualPartMap();
        EditPart rootPart = (EditPart) map.get(rootFigure);
        assertNotNull(findEntry(rootPart, "LED"));
    }
