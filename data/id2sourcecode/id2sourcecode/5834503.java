    public File getSaveFileName(File currentFile, File path, boolean saveAs) {
        File file = null;
        if (saveAs || (currentFile == null)) {
            file = getFile(path, false);
            if (file == null) {
                return null;
            }
            if (file.exists()) {
                Object[] options = { "Yes", "No" };
                int selection = JOptionPane.showOptionDialog(null, "File '" + file.getName() + "' already exists. Overwrite it?", "Warning!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                if (selection != 0) {
                    return null;
                }
            }
            file = addExtension(file);
            return file;
        } else {
            return currentFile;
        }
    }
