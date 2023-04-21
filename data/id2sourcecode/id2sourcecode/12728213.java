    public void createPartControl(Composite parent) {
        SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
        paletteViewer = createPaletteViewer(sashForm);
        graphicalViewer = createGraphicalViewer(sashForm);
        sashForm.setWeights(new int[] { 25, 75 });
    }
