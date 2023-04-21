    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        if (debug && getPaletteViewer() != null && getPaletteViewer().getControl() != null) {
            getPaletteViewer().getControl().setVisible(false);
        }
    }
