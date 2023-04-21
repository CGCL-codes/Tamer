    private IFigure getRoot(GraphicalViewer viewer) {
        GraphicalEditPart rootEditPart = (GraphicalEditPart) viewer.getRootEditPart();
        return getRoot(rootEditPart.getFigure());
    }
