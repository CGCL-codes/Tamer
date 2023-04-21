    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        IFile file = ((IFileEditorInput) getEditorInput()).getFile();
        RootModel root = null;
        if (file.exists()) {
            try {
                root = DiagramSerializer.deserialize(file.getContents());
                validateModel(root);
            } catch (Exception ex) {
                UMLPlugin.logException(ex);
            }
        }
        if (root == null) {
            root = createInitializeModel();
        }
        viewer.setContents(root);
        addDndSupport(viewer, getDiagramType());
        applyPreferences();
    }
