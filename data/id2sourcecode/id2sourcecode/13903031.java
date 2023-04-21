    public TiledDisplay(PMatrix2D plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Tiled Display");
        this.plot = plot;
        init();
    }
