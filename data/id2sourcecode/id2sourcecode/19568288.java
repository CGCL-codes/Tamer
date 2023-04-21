    public static RootEditPart getPaletteRootEditPart(GraphicalEditor editor) {
        GraphicalViewer viewer = getViewer(editor);
        if (viewer != null) return getPaletteRootEditPart(viewer);
        return null;
    }
