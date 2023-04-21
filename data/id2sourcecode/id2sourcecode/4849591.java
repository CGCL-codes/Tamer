    protected String getSaveDirPath(IEditorPart editorPart, GraphicalViewer viewer) {
        IFile file = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
        DirectoryDialog directoryDialog = new DirectoryDialog(editorPart.getEditorSite().getShell(), SWT.SAVE);
        IProject project = file.getProject();
        directoryDialog.setFilterPath(project.getLocation().toString());
        return directoryDialog.open();
    }
