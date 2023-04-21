    public BoxplotVariableSelector(BoxPlot1D plot1D) {
        super((JFrame) plot1D.getFigurePanel().getGraphicalViewer());
        setTitle("Variable Selector");
        this.plot = plot1D;
        init();
    }
