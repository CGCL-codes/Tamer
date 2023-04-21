    public boolean saveAs() throws Exception {
        try {
            boolean done = false;
            while (!done) {
                JFileChooser fc = new JFileChooser();
                File currentFile = getFile();
                if (currentFile == null) currentFile = new File(System.getProperty("user.dir"));
                fc.setCurrentDirectory(currentFile);
                fc.setName("Save as");
                int result = fc.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedfile = fc.getSelectedFile();
                    int overWriteDecision = -1;
                    if (selectedfile.exists()) overWriteDecision = JOptionPane.showConfirmDialog(this, "'" + selectedfile.getName() + "' already exists.  Overwrite ?", "File exists !", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (overWriteDecision == JOptionPane.YES_OPTION || overWriteDecision == -1) {
                        write(selectedfile);
                        document.setDocumentName(selectedfile.getName());
                        document.setDocumentNumber(-1);
                        document.setFile(selectedfile);
                        clean();
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception ex) {
            ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while saving document.");
        }
        return true;
    }
