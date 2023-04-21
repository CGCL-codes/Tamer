    public static boolean confirmOverwrite(File file, Component parent) {
        int result = JOptionPane.showConfirmDialog(parent, "File \"" + file.getName() + "\" already exists - overwrite?");
        if (result == 0) return true; else return false;
    }
