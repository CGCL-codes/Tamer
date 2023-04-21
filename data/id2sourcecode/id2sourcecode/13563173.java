    public ContourVariableSelector(VPDEScatter plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Variable Selector");
        this.plot = plot;
        init();
    }
