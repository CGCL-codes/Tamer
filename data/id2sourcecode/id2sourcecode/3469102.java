    private boolean overwiteExistingFile(File file) {
        if (file.exists()) {
            int res = JOptionPane.showConfirmDialog(null, "File " + file.getName() + " already exists.  Click\n" + "OK to overwrite and save.", "File exists", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }
