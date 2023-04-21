    public static void doIt() {
        isCanceled = true;
        File file = null;
        int ret = getFileChooser().showSaveDialog(JHelpDevFrame.getAJHelpDevToolFrame());
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            String fileName = file.toString();
            if (fileName.indexOf(".", fileName.lastIndexOf(File.separator)) < 0) {
                fileName = fileName + ".xml";
                file = new File(fileName);
            }
            if (new File(fileName).isFile()) {
                int option = JOptionPane.showConfirmDialog(JHelpDevFrame.getAJHelpDevToolFrame(), "File " + fileName + " already exists.\nDo you want to overwrite it?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            saveProjectToFile(fileName);
            Settings.getInstance().setLastDirectory(new File(fileName).getParent().toString());
            Settings.getInstance().insertRecentFile(new FileName(fileName));
            isCanceled = false;
        }
        return;
    }
