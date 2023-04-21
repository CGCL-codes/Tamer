    @Override
    public void createPartControl(Composite parent) {
        SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
        sashForm.setWeights(new int[] { 30, 70 });
        paletteViewer = createPaletteViewer(parent);
        graphicalViewer = new ScrollingGraphicalViewer();
    }
