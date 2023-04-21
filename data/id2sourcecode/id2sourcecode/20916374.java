    public static boolean exportAsImage(IEditorPart editorPart, GraphicalViewer graphicalViewer) {
        String path = getSaveFilePath(editorPart, graphicalViewer, -1);
        if (path != null) {
            int outputFormat = SWT.IMAGE_JPEG;
            if (path.toLowerCase().endsWith(".jpg")) {
                outputFormat = SWT.IMAGE_JPEG;
            } else if (path.toLowerCase().endsWith(".bmp")) {
                outputFormat = SWT.IMAGE_BMP;
            } else if (path.toLowerCase().endsWith(".png")) {
                outputFormat = SWT.IMAGE_PNG;
            } else if (path.toLowerCase().endsWith(".ico")) {
                outputFormat = SWT.IMAGE_ICO;
            }
            return exportAsImage(editorPart, graphicalViewer, path, outputFormat);
        } else {
            return false;
        }
    }
