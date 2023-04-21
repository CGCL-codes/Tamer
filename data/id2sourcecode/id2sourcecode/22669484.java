    public ClassColumnSelector(PMatrix2D plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Class Column Selector");
        this.plot = plot;
        init();
    }
