    private RootModel getRootModel() {
        GraphicalViewer viewer = (GraphicalViewer) getWorkbenchPart().getAdapter(GraphicalViewer.class);
        RootModel root = (RootModel) viewer.getRootEditPart().getContents().getModel();
        return root;
    }
