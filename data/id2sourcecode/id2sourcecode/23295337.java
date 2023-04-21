    public void doSave(IProgressMonitor monitor) {
        RootModel model = (RootModel) getGraphicalViewer().getContents().getModel();
        IFile file = ((IFileEditorInput) getEditorInput()).getFile();
        if (DBPlugin.getDefault().getPreferenceStore().getBoolean(DBPlugin.PREF_VALIDATE_ON_SAVE)) {
            try {
                file.deleteMarkers(IMarker.PROBLEM, false, 0);
                DiagramErrors errors = new DiagramValidator(model).doValidate();
                for (DiagramError error : errors.getErrors()) {
                    error.addMarker(file);
                }
            } catch (CoreException ex) {
                DBPlugin.logException(ex);
            }
        }
        try {
            needViewerRefreshFlag = false;
            file.setContents(VisualDBSerializer.serialize(model), true, true, monitor);
        } catch (Exception ex) {
            DBPlugin.logException(ex);
            throw new RuntimeException(ex);
        }
        getCommandStack().markSaveLocation();
    }
