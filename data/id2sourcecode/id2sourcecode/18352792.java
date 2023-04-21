    public static boolean confirmOverwrite(Shell shell, File file) {
        if (file.exists()) {
            return MessageDialog.openConfirm(shell, "Confirm", "File \"" + file + "\" already exists. Continue and overwrite?");
        }
        return true;
    }
