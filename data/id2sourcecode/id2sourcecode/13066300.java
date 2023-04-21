    public Point getLocation() {
        FigureCanvas canvas = (FigureCanvas) this.getGraphicalViewer().getControl();
        return canvas.getViewport().getViewLocation();
    }
