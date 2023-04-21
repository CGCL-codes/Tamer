    private static boolean checkOverwrite(final String path, final String fileName) {
        if (checkForFile(path, fileName)) {
            int answer = JOptionPane.showConfirmDialog(new JFrame(), fileName + " file already exists.  Overwrite?");
            if (answer == JOptionPane.YES_OPTION) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
