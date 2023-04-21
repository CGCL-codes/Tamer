    private static String getSaveFilePath(IEditorPart editorPart, GraphicalViewer viewer, int format) {
        FileDialog fileDialog = new FileDialog(editorPart.getEditorSite().getShell(), SWT.SAVE);
        String[] filterExtensions = new String[] { Messages.getString("org.isistan.flabot.util.ImageSaveUtil.jpeg"), Messages.getString("org.isistan.flabot.util.ImageSaveUtil.bmp"), Messages.getString("org.isistan.flabot.util.ImageSaveUtil.ico") };
        if (format == SWT.IMAGE_BMP) filterExtensions = new String[] { Messages.getString("org.isistan.flabot.util.ImageSaveUtil.bmp") }; else if (format == SWT.IMAGE_JPEG) filterExtensions = new String[] { Messages.getString("org.isistan.flabot.util.ImageSaveUtil.jpeg") }; else if (format == SWT.IMAGE_ICO) filterExtensions = new String[] { Messages.getString("org.isistan.flabot.util.ImageSaveUtil.ico") };
        fileDialog.setFilterExtensions(filterExtensions);
        return fileDialog.open();
    }
