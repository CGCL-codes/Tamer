    public org.eclipse.draw2d.geometry.Rectangle getViewportBounds() {
        FigureCanvas canvas = (FigureCanvas) this.getGraphicalViewer().getControl();
        return canvas.getViewport().getBounds();
    }
