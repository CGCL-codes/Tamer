    private boolean checkWrite() {
        File file = getSelectedFile();
        if (file.exists() && !file.isDirectory()) {
            boolean canWrite;
            try {
                canWrite = file.canWrite();
            } catch (SecurityException e) {
                canWrite = false;
            }
            if (canWrite) {
                SystemSounds.warning();
                return JOptionPane.showConfirmDialog(this, "The file " + file.getName() + " already exists.  Do you want to replace the existing file?", "Overwrite File?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
            } else {
                SystemSounds.error();
                JOptionPane.showMessageDialog(this, "The file " + file.getName() + " already exists and cannot be overwritten.\n" + "Check that the file is not marked as read only, that you have sufficient security permissions to write data into the file " + "and that Java is allowed to write files.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            return true;
        }
    }
