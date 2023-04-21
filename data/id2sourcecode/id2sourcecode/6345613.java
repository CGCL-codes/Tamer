    public void saveSecureFile() {
        Enumeration files = fileList.getPoolContent();
        if (files.hasMoreElements()) {
            XmlFile file = (XmlFile) files.nextElement();
            String fileName = editor.getXmlFilesManager().nameNewFileAsSecureFile();
            File newFile = new File(fileName);
            if (newFile != null) {
                if (newFile.exists()) {
                    Object[] options = { "Yes", "No" };
                    int answer = JOptionPane.showOptionDialog(this.getEditor(), "This file already exists.\nDo you want to overwrite it ?", "Existing file", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
                    if (answer != 1) {
                        try {
                            SDMainFrame.stringToFile(file.getContent(), fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        SDMainFrame.stringToFile(file.getContent(), fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
