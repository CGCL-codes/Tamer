    public void doSave(IProgressMonitor monitor) {
        try {
            IEditorInput input = getEditorInput();
            if (input instanceof IFileEditorInput) {
                needViewerRefreshFlag = false;
                IFile file = ((IFileEditorInput) input).getFile();
                file.setContents(DiagramSerializer.serialize((RootModel) getGraphicalViewer().getContents().getModel()), true, true, monitor);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        getCommandStack().markSaveLocation();
    }
