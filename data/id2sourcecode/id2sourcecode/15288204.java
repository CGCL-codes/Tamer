    private void refreshGraphicalViewer() {
        IEditorInput input = getEditorInput();
        if (input instanceof IFileEditorInput) {
            try {
                IFile file = ((IFileEditorInput) input).getFile();
                GraphicalViewer viewer = getGraphicalViewer();
                RootModel newRoot = null;
                try {
                    newRoot = DiagramSerializer.deserialize(file.getContents());
                } catch (Exception ex) {
                    UMLPlugin.logException(ex);
                    return;
                }
                RootModel root = (RootModel) viewer.getContents().getModel();
                root.copyFrom(newRoot);
            } catch (Exception ex) {
                UMLPlugin.logException(ex);
            }
        }
    }
