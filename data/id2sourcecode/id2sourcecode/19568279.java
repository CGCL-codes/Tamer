    public static IFigure getRootFigure(GraphicalViewer viewer) {
        RootEditPart rootEditPart = viewer.getRootEditPart();
        if (rootEditPart != null) return ((GraphicalEditPart) rootEditPart).getFigure();
        return null;
    }
