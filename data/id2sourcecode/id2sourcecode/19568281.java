    public static RootEditPart getPaletteRootEditPart(GraphicalViewer viewer) {
        PaletteViewer paletteViewer = getPaletteViewer(viewer);
        if (paletteViewer != null) return paletteViewer.getRootEditPart();
        return null;
    }
