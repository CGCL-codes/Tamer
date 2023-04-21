    public ContourDialog(PMatrix2D plot) {
        super((JFrame) plot.getFigurePanel().getGraphicalViewer());
        this.plot = plot;
        css = new JSlider(0, 50, this.plot.getContourSteps());
        css.setMajorTickSpacing(10);
        css.setMinorTickSpacing(1);
        css.setPaintTicks(true);
        css.setPaintLabels(true);
        css.addChangeListener(new SliderListener());
        add(css);
        pack();
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
