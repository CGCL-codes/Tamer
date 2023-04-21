    public static String getSaveFilePath(IEditorPart editorPart, GraphicalViewer viewer, int format) {
        FileDialog fileDialog = new FileDialog(editorPart.getEditorSite().getShell(), SWT.SAVE);
        String[] extensions = new String[] { "*.jpg", "*.bmp", "*.ico", "*.png" };
        if (format == SWT.IMAGE_BMP) {
            extensions = new String[] { "*.bmp" };
        } else if (format == SWT.IMAGE_JPEG) {
            extensions = new String[] { "*.jpg" };
        } else if (format == SWT.IMAGE_PNG) {
            extensions = new String[] { "*.png" };
        } else if (format == SWT.IMAGE_ICO) {
            extensions = new String[] { "*.ico" };
        }
        fileDialog.setFilterExtensions(extensions);
        return fileDialog.open();
    }
