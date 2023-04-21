    public void createPalette() {
        parent.getEditDomain().setPaletteRoot(PaletteProvider.createPalette());
        paletteContainer = new FlyoutPaletteComposite(this, SWT.NONE, parent.getSite().getPage(), createPaletteViewProvieder(), getPalettePreferences());
        createGraphicalViewer();
        paletteContainer.setGraphicalControl(graphicalViewer.getControl());
    }
