    public SurfaceDialog(VSurface3D surf) {
        super((JFrame) surf.getFigurePanel().getGraphicalViewer());
        setTitle("Surface Settings");
        this.plot = surf;
        if (plot != null) this.jRenderer3D = plot.jRenderer3D;
        add(createSettingsPanelRight());
        setSize(120, 450);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
