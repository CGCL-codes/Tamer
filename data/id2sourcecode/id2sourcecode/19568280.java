    public static PaletteViewer getPaletteViewer(GraphicalViewer viewer) {
        if (viewer instanceof PaletteViewer) return (PaletteViewer) viewer;
        EditDomain editDomain = viewer.getEditDomain();
        if (editDomain != null) return editDomain.getPaletteViewer();
        return null;
    }
