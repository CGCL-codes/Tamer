    private boolean canWriteFile(File file) {
        if (file.exists()) {
            int overwrite = JOptionPane.showConfirmDialog(null, "A file named \"" + file.getName() + "\" already exists.\nDo you want to replace it?", "Overwrite file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (overwrite != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        return true;
    }
