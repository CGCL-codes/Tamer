    protected boolean saveAs(Object source, AssessmentDocument document) {
        File file = document.getRoot().getSourceFile();
        if (file != null) fileChooser.setSelectedFile(file); else fileChooser.setSelectedFile(new File(""));
        if (fileChooser.showSaveDialog(mainFrame) != JFileChooser.APPROVE_OPTION) return false;
        file = fileChooser.getSelectedFile();
        if (file == null) return false;
        if (file.exists() && !file.equals(document.getRoot().getSourceFile())) {
            Object[] options = new Object[] { "Yes", "No" };
            Object initialOption = "No";
            int returnValue = JOptionPane.showOptionDialog(mainFrame, "File already exists. Overwrite?", "Overwrite", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, initialOption);
            if (returnValue != JOptionPane.YES_OPTION) return false;
        }
        return save(source, document, file);
    }
