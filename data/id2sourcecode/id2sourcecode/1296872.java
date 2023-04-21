    private boolean checkWrite() {
        final File file = this.getSelectedFile();
        boolean isDir, exists;
        try {
            exists = file.exists();
            isDir = file.isDirectory();
        } catch (final SecurityException e) {
            exists = false;
            isDir = false;
        }
        if (!isDir) {
            boolean canWrite;
            try {
                canWrite = file.canWrite();
            } catch (final SecurityException e) {
                canWrite = false;
            }
            if (exists && canWrite) {
                SystemSounds.warning();
                return JOptionPane.showConfirmDialog(this, "The file " + file.getName() + " already exists.  Do you want to replace the existing file?", "Overwrite File?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
            } else if (!canWrite) {
                SystemSounds.error();
                JOptionPane.showMessageDialog(this, "The file " + file.getName() + " cannot be written to.\n" + "Check that the file is not marked as read-only and that you have sufficient security permissions.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
