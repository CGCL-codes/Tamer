    public static PaletteViewer getPaletteViewer(GraphicalEditor editor) {
        GraphicalViewer viewer = getViewer(editor);
        if (viewer != null) return getPaletteViewer(viewer);
        return null;
    }
