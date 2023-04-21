    public SphereRadiusDialog(VPDEScatter plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        add(new ParetoRadiusPanel());
        setSize(365, 255);
        this.plot = plot;
        if (pdens != null) {
            pdens = plot.getParetoDensity();
            initialPercentileValue = pdens.getPercentile();
            initialNumberOfClusters = pdens.getClusters();
            initialRadius = pdens.getRadius();
        }
        addComponentListener(this);
    }
