    private boolean checkOverwrite(File destination) {
        if (!destination.exists()) return true;
        int ch = JOptionPane.showConfirmDialog(this, Language.string("A file with the same name already exists ({0}): confirm overwrite?", destination.getName()), Language.string("Overwrite?"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ch != JOptionPane.YES_OPTION) {
            return false;
        } else {
            return true;
        }
    }
