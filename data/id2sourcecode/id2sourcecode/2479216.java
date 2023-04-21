    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String fileName = "";
        File file = null;
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setCurrentDirectory(new java.io.File(Resources.getApplicationDirectoryCanonicalPath()));
        fc.setDialogTitle("New Index Directory");
        fc.setApproveButtonToolTipText("Create Index Directory");
        while (true) {
            int returnVal = fc.showDialog(this, "Create");
            if (returnVal == JFileChooser.CANCEL_OPTION) {
                return;
            } else if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                try {
                    fileName = file.getCanonicalPath();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (file.exists()) {
                    Object[] options = { "Open Index", "Overwrite Index" };
                    int opt = JOptionPane.showOptionDialog(this, "Index directory '" + fileName + "' already exists.\n" + "Do you wish to open existing Index or Overwrite?\n" + "(Choosing to overwrite will delete existing Index)", "Error Creating Index Directory", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                    if (opt == JOptionPane.NO_OPTION) {
                        if (Util.deleteDir(file) == false) {
                            JOptionPane.showMessageDialog(this, "Directory '" + fileName + "' cannot be deleted.", "Error Creating Index Directory", JOptionPane.ERROR_MESSAGE, this.imageControl.getErrorIcon());
                        }
                    }
                }
                this.close();
                try {
                    Resources.setIndex(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.init();
                this.indexPropertiesPanel.setProperties(this.indexProperties);
                return;
            }
        }
    }
