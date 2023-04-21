    public int getVerticalScroll() {
        FigureCanvas canvas = (FigureCanvas) this.getGraphicalViewer().getControl();
        return canvas.getViewport().getVerticalRangeModel().getValue();
    }
