    public GraphicalEditPart findEditPart(IFigure figure) {
        checkFigure(figure);
        GraphicalViewer viewer = findViewer(figure);
        if (viewer != null) return (GraphicalEditPart) viewer.getVisualPartMap().get(figure);
        return null;
    }
