    public BandWidthDialog(KDEPlot plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        setTitle("Bandwidth Selection");
        this.plot = plot;
        add(new BandwidthSelector());
        setSize(220, 140);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
