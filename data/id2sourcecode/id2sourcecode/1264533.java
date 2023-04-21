    public Scatter3DVariableSelector(ScatterPlot3D plot3D) {
        super((JFrame) plot3D.getFigurePanel().getGraphicalViewer());
        setTitle("Variable Selector");
        plot = plot3D;
        init();
    }
