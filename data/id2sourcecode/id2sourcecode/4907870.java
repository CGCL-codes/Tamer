    public void doSaveAs() {
        if (!isValidReport()) {
            return;
        }
        Shell shell = getSite().getShell();
        IPath path;
        final Object opFile;
        if (fileObject instanceof IFile) {
            SaveAsDialog saveAsDialog = new SaveAsDialog(shell);
            saveAsDialog.setOriginalFile((IFile) fileObject);
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
            fileDialog.setFilterPath(((File) fileObject).getPath());
            fileDialog.setFileName(((File) fileObject).getName());
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
                try {
                    writeReportToFile(opFile);
                } catch (Exception ex) {
                    StatusInfo i = new StatusInfo(IStatus.ERROR, "" + ex.getMessage());
                    throw new CoreException(i);
                }
            }
        };
        try {
            editorSaving = true;
            (new ProgressMonitorDialog(getSite().getShell())).run(false, false, modifyOperation);
            setInput(editorInput);
            getGraphicalViewer().setContents(report);
            flushStack();
            actionActivity = false;
            getCommandStack().markSaveLocation();
        } catch (Exception exception) {
        } finally {
            editorSaving = false;
        }
        return;
    }
