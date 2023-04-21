    public void setLocation(int x, int y) {
        FigureCanvas canvas = (FigureCanvas) this.getGraphicalViewer().getControl();
        canvas.scrollTo(x, y);
    }
