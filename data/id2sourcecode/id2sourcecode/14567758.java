    public static String selectImageNameSave(final String sTitle, final String sCurDir, final String sCurFile) {
        assert sTitle != null;
        assert sCurDir != null;
        assert sCurFile != null;
        String sFileName = showImgFileChooserSave(sTitle, sCurDir, sCurFile);
        if (sFileName != null) {
            File f = new File(sFileName);
            if (f.exists()) {
                int r = ServiceFactory.getInstance().readUsrYesNoCancel(sFileName + " already exists.\n\nOverwrite existing file?");
                if (r == BaseDlg.CANCEL) {
                } else if (r == BaseDlg.NO) {
                    sFileName = selectImageNameSave(sTitle, sCurDir, sCurFile);
                } else if (r == BaseDlg.YES) {
                }
            }
        }
        return sFileName;
    }
