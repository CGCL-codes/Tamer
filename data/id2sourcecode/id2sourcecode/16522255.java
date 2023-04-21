    public VariableSelector(KDEPlot plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Variable Selector");
        this.plot = plot;
        init();
    }
