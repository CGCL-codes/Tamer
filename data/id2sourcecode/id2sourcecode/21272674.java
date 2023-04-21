    private boolean isShowingChains() {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        Boolean val = (Boolean) viewer.getProperty(IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN);
        if (val != null) return val.booleanValue();
        return false;
    }
