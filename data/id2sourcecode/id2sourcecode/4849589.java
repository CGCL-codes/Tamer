    protected String getSaveFilePath(IEditorPart editorPart, GraphicalViewer viewer) {
        IFile file = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
        FileDialog fileDialog = new FileDialog(editorPart.getEditorSite().getShell(), SWT.SAVE);
        IProject project = file.getProject();
        fileDialog.setFilterPath(project.getLocation().toString());
        String[] filterExtensions = this.getFilterExtensions();
        fileDialog.setFilterExtensions(filterExtensions);
        String fileName = this.getDiagramFileName(editorPart);
        fileDialog.setFileName(fileName);
        return fileDialog.open();
    }
