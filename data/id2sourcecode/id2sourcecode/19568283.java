    public static PaletteRoot getPaletteRoot(GraphicalViewer viewer) {
        PaletteViewer paletteViewer = getPaletteViewer(viewer);
        if (paletteViewer != null) return paletteViewer.getPaletteRoot();
        return null;
    }
