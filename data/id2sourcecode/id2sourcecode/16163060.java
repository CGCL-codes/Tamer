    public Object getAdapter(Class type) {
        if (type == IContentOutlinePage.class) return new CsdeOutlinePage(new TreeViewer()); else if (type == ZoomManager.class) return getGraphicalViewer().getProperty(ZoomManager.class.toString()); else if (type == TreeEditor.class) return new TreeEditor(new TreeViewer()); else if (type == PalettePage.class) {
            PaletteViewerPage paletteViewerPage = new PaletteViewerPage(getPaletteViewerProvider());
            return paletteViewerPage;
        } else return super.getAdapter(type);
    }
