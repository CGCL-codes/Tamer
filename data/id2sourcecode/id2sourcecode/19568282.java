    public static IFigure getPaletteRootFigure(GraphicalViewer viewer) {
        RootEditPart rootEditPart = getPaletteRootEditPart(viewer);
        if (rootEditPart != null) return ((GraphicalEditPart) rootEditPart).getFigure();
        return null;
    }
