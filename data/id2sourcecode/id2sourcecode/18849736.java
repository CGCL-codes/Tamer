    @Override
    protected boolean isOverwriteOk(String fileName, String displayFileName, String title) {
        boolean overwriteOk = true;
        File existingFile = new File(fileName);
        if (existingFile != null && existingFile.exists()) {
            int option = JOptionPane.showConfirmDialog(this, "A file with the name " + displayFileName + " already exists.\n" + "Do you want to overwrite it?", title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            overwriteOk = (option == JOptionPane.YES_OPTION);
        }
        return overwriteOk;
    }
