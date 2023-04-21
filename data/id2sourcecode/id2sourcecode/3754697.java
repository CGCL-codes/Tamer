    private String selectFileSaveAs(String title, String fileType, String newFileName) {
        File lastDirFile = new File(getSaveDir());
        if (newFileName != null) {
            lastDirFile = new File(getSaveDir() + "/" + newFileName);
        } else {
            lastDirFile = new File(getSaveDir() + "/untitled." + fileType);
        }
        JFileChooser fd = null;
        if (view == null) {
            fd = new JFileChooser();
        } else {
            fd = new JFileChooser(view);
        }
        fd.setDialogTitle("Save As " + title);
        fd.setSelectedFile(lastDirFile);
        File result = null;
        if (fd.showSaveDialog(owner) != JFileChooser.CANCEL_OPTION) {
            result = fd.getSelectedFile();
        }
        String fileName = null;
        if (result != null) {
            if (result.exists()) {
                if (JOptionPane.showConfirmDialog(owner, "The file " + result.getName() + " alread exists. Do you wish to Overwrite it?") != JOptionPane.YES_OPTION) {
                    return null;
                }
            }
            fileName = org.one.stone.soup.file.FileHelper.getPath(result.getPath());
            fileName += result.getName();
            lastDirs.put(title, FileHelper.getPath(result.getPath()));
        }
        return fileName;
    }
