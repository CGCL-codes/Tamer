    public boolean confirmOverwrite(final File file) {
        String message = "The file \"" + file.getName() + "\" already exists. Overwrite?";
        Object[] options = { "OK", "Cancel" };
        return JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(null, message, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    }
