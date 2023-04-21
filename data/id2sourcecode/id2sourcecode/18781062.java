    protected void createPageControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setBackground(parent.getBackground());
        composite.setLayout(new GridLayout(2, false));
        createPaletteViewer(composite);
        GridData gd = new GridData(GridData.FILL_VERTICAL);
        gd.widthHint = 125;
        getPaletteViewer().getControl().setLayoutData(gd);
        createGraphicalViewer(composite);
        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 275;
        getGraphicalViewer().getControl().setLayoutData(gd);
    }
