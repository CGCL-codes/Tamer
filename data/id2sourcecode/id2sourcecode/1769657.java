    public boolean nameAndSaveFile(XmlFile file) {
        XmlFile newFile = null;
        if (newFile != null) {
            if (newFile.exists()) {
                Object[] options = { "Yes", "No" };
                int answer = JOptionPane.showOptionDialog(AdminConsole.getInstance(), "This file already exists.\nDo you want to overwrite it ?", "Existing file", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                if (answer == 1) {
                    return false;
                }
            }
            fileChanged(newFile);
            XmlFileManager.getInstance().closeFile(xmlFile);
            newFile.save();
            return true;
        }
        return false;
    }
