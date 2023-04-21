    public PPDEPlotVariableSelector(PDEPlot plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Variable Selector");
        this.plot = plot;
        init();
    }
