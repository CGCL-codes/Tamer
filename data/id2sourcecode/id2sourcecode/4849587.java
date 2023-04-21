    protected void save(IEditorPart editorPart, GraphicalViewer viewer) throws Exception {
        String saveFilePath = this.getSaveFilePath(editorPart, viewer);
        if (saveFilePath == null) {
            return;
        }
        File file = new File(saveFilePath);
        if (file.exists()) {
            MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            messageBox.setText(ResourceString.getResourceString("dialog.title.warning"));
            messageBox.setMessage(ResourceString.getResourceString(this.getConfirmOverrideMessage()));
            if (messageBox.open() == SWT.CANCEL) {
                return;
            }
        }
        this.save(editorPart, viewer, saveFilePath);
        this.refreshProject();
    }
