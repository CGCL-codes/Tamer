    public java.io.File saveFileAs(java.io.File p_workingDirectory, String p_addTitle) {
        java.io.File file;
        javax.swing.JFileChooser fd = new javax.swing.JFileChooser();
        fd.setCurrentDirectory(p_workingDirectory);
        fd.setDialogTitle(p_addTitle);
        net.hussnain.io.RabtFileFilter filter = new net.hussnain.io.RabtFileFilter("Accepted Documents");
        filter.addFileExtention(getDocFormat().toString(), getDocDescription());
        fd.addChoosableFileFilter(filter);
        net.hussnain.utils.UIUtil.setLocationToMid(fd);
        while (true) {
            int action = fd.showSaveDialog(null);
            file = fd.getSelectedFile();
            if (file == null) {
                if (action == fd.APPROVE_OPTION) JOptionPane.showMessageDialog(null, "There is a problem with the target file, please try again later.", "Saving File", JOptionPane.WARNING_MESSAGE);
                break;
            }
            file = Utilities.assignFileExtension(file, getDocFormat().toString());
            if (!(action == fd.APPROVE_OPTION)) {
                break;
            } else {
                if (!file.exists()) {
                    saveFile(file, textPane);
                    logger.info("file saved to " + file.getAbsolutePath());
                    break;
                } else {
                    int value = JOptionPane.showConfirmDialog(this, "Another file with the same name already exists.\nDo you want to overwrite this existing one?", "Saveing the File", JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        saveFile(file, textPane);
                        logger.info("file saved to " + file.getAbsolutePath());
                        break;
                    }
                }
            }
        }
        return file;
    }
