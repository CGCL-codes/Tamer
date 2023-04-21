    public void doSaveAs() {
        if (!isValidDiagram()) {
            return;
        }
        Shell shell = getSite().getShell();
        IPath path;
        final Object opFile;
        if (file instanceof IFile) {
            SaveAsDialog saveAsDialog = new SaveAsDialog(shell);
            saveAsDialog.setOriginalFile((IFile) file);
            if (saveAsDialog.open() != 0) {
                return;
            }
            path = saveAsDialog.getResult();
            if (path == null) {
                return;
            }
            opFile = getWorkspaceRoot().getFile(path);
        } else {
            FileDialog fileDialog = new FileDialog(shell);
            fileDialog.setText("Save As");
            fileDialog.setFilterPath(((File) file).getPath());
            fileDialog.setFileName(((File) file).getName());
            String filePath = fileDialog.open();
            if (filePath == null) {
                return;
            }
            path = new Path(filePath);
            opFile = path.toFile();
        }
        IEditorInput editorInput = EditorInputFactory.getEditorInput(opFile);
        WorkspaceModifyOperation modifyOperation = new WorkspaceModifyOperation() {

            public void execute(IProgressMonitor iprogressmonitor) throws CoreException {
                storeToXMLFile(opFile);
            }
        };
        try {
            editorSaving = true;
            (new ProgressMonitorDialog(getSite().getShell())).run(false, false, modifyOperation);
            setInput(editorInput);
            getGraphicalViewer().setContents(diagram);
            flushStack();
            actionActivity = false;
            getCommandStack().markSaveLocation();
        } catch (Exception exception) {
        } finally {
            editorSaving = false;
        }
        return;
    }
