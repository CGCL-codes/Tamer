    private boolean checkDirectory(File directory) {
        if (directory.exists()) {
            if (directory.isDirectory()) {
                int overwriteReturn = JOptionPane.showConfirmDialog(null, directory.getName() + " already exists.\nResume writing in the same folder?", "Write into existing directory?", JOptionPane.YES_NO_OPTION);
                if (overwriteReturn == JOptionPane.NO_OPTION) {
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Could not create directory " + directory.getPath() + "\nA file with that name already exists!", "Create Directory error", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        } else {
            int createDir = JOptionPane.showConfirmDialog(null, directory.getPath() + " does NOT exist. Directory will be created", "Create new directory?", JOptionPane.YES_NO_OPTION);
            if (createDir == JOptionPane.YES_OPTION) {
                if (!createDirectory(directory)) {
                    JOptionPane.showMessageDialog(null, "Error: Could not create directory " + directory.getPath(), "Create Directory error", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
