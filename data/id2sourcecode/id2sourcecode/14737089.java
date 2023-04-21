    void jButton2_actionPerformed(ActionEvent e) {
        String fname;
        fname = "";
        JFileChooser fdial = new JFileChooser();
        fdial.setDialogTitle("Save as...");
        fdial.setCurrentDirectory(new File(Tools.directoryOfFile(this.getActualFile())));
        fdial.setSelectedFile(new File(this.getActualFile()));
        fdial.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fdial.setDialogType(JFileChooser.SAVE_DIALOG);
        fdial.addChoosableFileFilter(SimpleFileFilter.DEFAULT);
        fdial.addChoosableFileFilter(SimpleFileFilter.COMPRESSED);
        fdial.setFileFilter(SimpleFileFilter.DEFAULT);
        int uresp = fdial.showSaveDialog(this);
        File fln = fdial.getSelectedFile();
        if (uresp == JFileChooser.APPROVE_OPTION && fln != null) {
            fname = fln.getAbsolutePath();
            try {
                boolean cont = true;
                if (new File(fname).exists()) {
                    cont = (JOptionPane.showConfirmDialog(this, "The file " + fname + "\nalready exists...\nOverwrite it?", "Existing file...", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
                }
                if (cont) {
                    SourceFile sfile = new SourceFile(fname);
                    sfile.setSource(jTextArea1.getText());
                    sfile.save();
                    referenceSource_ = jTextArea1.getText();
                    jTextArea2.write(fln + " saved...\n");
                    this.setActualFile(fname);
                } else {
                    jTextArea2.write("cancelled...\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    jTextArea2.write("Warning : IOException error... Saving abstrasy File.\n");
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
