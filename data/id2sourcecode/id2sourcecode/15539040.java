    private boolean mustBeOverWritten(String fileName) {
        String path = outPathText.getText();
        String fullName = path + "/" + fileName;
        File file = new File(fullName);
        if (file.exists()) {
            if (JOptionPane.showConfirmDialog(this, fileName + " already exists !\n" + "Overwrite it ?", "Confirmation Window", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
                return true;
            }
        }
        return false;
    }
