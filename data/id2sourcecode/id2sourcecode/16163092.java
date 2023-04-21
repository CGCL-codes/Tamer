    public void addPaletteOperator(PaletteOperator newPaletteOperator) {
        if (paletteOperator != null) {
            getGraphicalViewer().removeSelectionChangedListener(paletteOperator);
            paletteOperator = newPaletteOperator;
        }
        GraphicalViewer viewer = getGraphicalViewer();
        if (viewer != null) viewer.addSelectionChangedListener(paletteOperator);
    }
