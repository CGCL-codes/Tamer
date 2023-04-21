    public static boolean save(IEditorPart editorPart, GraphicalViewer viewer, String saveFilePath, int format) {
        Assert.isNotNull(editorPart, "null editorPart passed to ImageSaveUtil::save");
        Assert.isNotNull(viewer, "null viewer passed to ImageSaveUtil::save");
        Assert.isNotNull(saveFilePath, "null saveFilePath passed to ImageSaveUtil::save");
        if (format != SWT.IMAGE_BMP && format != SWT.IMAGE_JPEG && format != SWT.IMAGE_ICO) throw new IllegalArgumentException(Messages.getString("org.isistan.flabot.edit.ImageSaveUtil.exceptionName"));
        try {
            saveEditorContentsAsImage(editorPart, viewer, saveFilePath, format);
        } catch (Exception ex) {
            MessageDialog.openError(editorPart.getEditorSite().getShell(), Messages.getString("org.isistan.flabot.edit.ImageSaveUtil.dialogName"), Messages.getString("org.isistan.flabot.edit.ImageSaveUtil.dialogDescription") + ex);
            return false;
        }
        return true;
    }
