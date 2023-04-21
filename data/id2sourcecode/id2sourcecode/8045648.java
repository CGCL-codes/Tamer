    protected boolean canSaveFile(File file) {
        if (file.exists()) {
            if (file.canWrite()) {
                if (JOptionPane.showConfirmDialog(getParent(), "File already exists. Do you want to overwrite it?", "File Exists", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                    return true;
                } else {
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(getParent(), "Cannot write to file " + file.getName(), "Check File ", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }
