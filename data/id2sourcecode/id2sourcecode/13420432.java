    public QQPlot_Attrib_vs_Distr(QQPlot2D plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Variable / Distribution Selector");
        this.plot = plot;
        init();
    }
