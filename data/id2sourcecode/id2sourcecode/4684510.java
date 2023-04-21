    public int getHorizontalScroll() {
        FigureCanvas canvas = (FigureCanvas) this.getGraphicalViewer().getControl();
        return canvas.getViewport().getHorizontalRangeModel().getValue();
    }
