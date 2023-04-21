    public QQPlotVariableSelector(QQPlot2D plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Variable Selector");
        this.plot = plot;
        init();
    }
