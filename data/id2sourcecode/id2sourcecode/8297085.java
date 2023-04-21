    public void arrageViewerToPosition(int x, int y) {
        FigureCanvas control = (FigureCanvas) getGraphicalViewer().getControl();
        control.getViewport().setHorizontalLocation(x - control.getHorizontalBar().getThumb() / 2);
        control.getViewport().setVerticalLocation(y - control.getVerticalBar().getThumb() / 2);
    }
