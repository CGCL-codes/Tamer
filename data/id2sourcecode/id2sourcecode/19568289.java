    public static IFigure getPaletteRootFigure(GraphicalEditor editor) {
        GraphicalViewer viewer = getViewer(editor);
        if (viewer != null) return getPaletteRootFigure(viewer);
        return null;
    }
