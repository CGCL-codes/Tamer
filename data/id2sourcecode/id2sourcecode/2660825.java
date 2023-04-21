    @Override
    protected void initializeGraphicalViewer() {
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getModel());
        IEditorInput input = getEditorInput();
        if (input instanceof FileEditorInput) {
            FileEditorInput fileInput = (FileEditorInput) input;
            mEditedFile = fileInput.getFile();
            LayoutReloadMonitor.getMonitor().addListener(mEditedFile.getProject(), this);
        } else {
            mEditedFile = null;
            AdtPlugin.log(IStatus.ERROR, "Input is not of type FileEditorInput: %1$s", input.toString());
        }
    }
