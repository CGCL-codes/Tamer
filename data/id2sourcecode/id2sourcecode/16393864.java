    @Override
    protected void save(IEditorPart editorPart, GraphicalViewer viewer, String saveFilePath) {
        ProgressMonitorDialog monitor = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        try {
            if (outputImage(monitor, viewer, saveFilePath) != -1) {
                Activator.showMessageDialog("dialog.message.export.finish");
            }
        } catch (InterruptedException e) {
        }
    }
