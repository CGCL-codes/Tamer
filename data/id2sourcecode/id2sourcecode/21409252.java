    public boolean saveFileAs(String newName) {
        boolean result = true;
        File theFile = new File(newName);
        if (theFile.exists()) {
            result = false;
            int reply = JOptionPane.showConfirmDialog(null, theFile.getName() + " already exists. Overwrite it?", "File exists", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) result = true;
        }
        if (result) result = writeFile(newName);
        return result;
    }
