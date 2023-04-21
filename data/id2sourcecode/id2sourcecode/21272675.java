    @Override
    public void run() {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        boolean val = !isShowingChains();
        viewer.setProperty(IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN, new Boolean(val));
        setText(isShowingChains() ? Messages.ShowStructuralChainsAction_1 : DEFAULT_TEXT);
    }
