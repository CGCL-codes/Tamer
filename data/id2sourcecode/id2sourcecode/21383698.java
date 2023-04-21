    public Scatter2DColorSelector(vademecum.visualizer.D2.scatter.ScatterPlot2D plot2D) {
        super((JFrame) plot2D.getFigurePanel().getGraphicalViewer());
        setTitle("Color Selector");
        scatter2D = plot2D;
        init();
    }
