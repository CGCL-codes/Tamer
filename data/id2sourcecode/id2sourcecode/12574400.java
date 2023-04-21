    public static boolean save(IEditorPart editorPart, GraphicalViewer viewer) {
        Assert.isNotNull(editorPart, "null editorPart passed to ImageSaveUtil::save");
        Assert.isNotNull(viewer, "null viewer passed to ImageSaveUtil::save");
        String saveFilePath = getSaveFilePath(editorPart, viewer, -1);
        if (saveFilePath == null) return false;
        int format = SWT.IMAGE_JPEG;
        if (saveFilePath.endsWith(".jpeg")) format = SWT.IMAGE_JPEG; else if (saveFilePath.endsWith(".bmp")) format = SWT.IMAGE_BMP; else if (saveFilePath.endsWith(".ico")) format = SWT.IMAGE_ICO;
        return save(editorPart, viewer, saveFilePath, format);
    }
