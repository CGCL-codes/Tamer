    private synchronized boolean confirmOverwrite(String filename) {
        int option = JOptionPane.showConfirmDialog(playerView, "File '" + filename + "' already exists.  Overwrite?", "File Exists", JOptionPane.OK_CANCEL_OPTION);
        return option == JOptionPane.OK_OPTION;
    }
