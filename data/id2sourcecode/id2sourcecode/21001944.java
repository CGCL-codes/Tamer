    public File getSelectedFileWithFormatExt() {
        try {
            File file = getSelectedFile();
            file = addExtensionIfNeeded(file);
            if (file != null && file.exists()) {
                int option = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "File Already Exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option != JOptionPane.YES_OPTION) {
                    return null;
                }
            }
            return file;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null, "The filename could not be resolved.\n" + ioe.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
