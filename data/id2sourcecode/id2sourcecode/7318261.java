    @Override
    protected String getSaveFilePath(IEditorPart editorPart, GraphicalViewer viewer) {
        IFile file = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
        DirectoryDialog fileDialog = new DirectoryDialog(editorPart.getEditorSite().getShell(), SWT.SAVE);
        IProject project = file.getProject();
        fileDialog.setFilterPath(project.getLocation().toString());
        fileDialog.setMessage(ResourceString.getResourceString("dialog.message.export.html.dir.select"));
        String saveFilePath = fileDialog.open();
        if (saveFilePath != null) {
            saveFilePath = saveFilePath + OUTPUT_DIR;
        }
        return saveFilePath;
    }
