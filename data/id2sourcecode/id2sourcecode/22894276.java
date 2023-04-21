    private void cmdSaveScriptActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            JFileChooser fc = null;
            if (this.currentOutputDirectory != null) {
                fc = new JFileChooser(this.currentOutputDirectory);
            } else {
                String lastOutputDir = this.preferences.get("LAST_OUTPUT_DIR", "");
                if (lastOutputDir != null && (!lastOutputDir.trim().equals(""))) {
                    fc = new JFileChooser(lastOutputDir);
                } else {
                    if (this.currentInputDirectory != null) {
                        fc = new JFileChooser(this.currentInputDirectory.getPath());
                    } else {
                        fc = new JFileChooser();
                    }
                }
            }
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileFilter fileFilter = new FileNameExtensionFilter("Script SQL", "sql");
            fc.addChoosableFileFilter(fileFilter);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setMultiSelectionEnabled(false);
            fc.setFileFilter(fileFilter);
            int res = fc.showDialog(this, resourceBundle.getString("save.as"));
            this.currentOutputDirectory = fc.getCurrentDirectory();
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                this.preferences.put("LAST_OUTPUT_DIR", this.currentOutputDirectory.getAbsolutePath());
                String fileName = file.getPath();
                if (!fileName.endsWith(".sql")) {
                    fileName = fileName + ".sql";
                    file = new File(fileName);
                }
                int saveOption = JOptionPane.YES_OPTION;
                if (file.exists()) {
                    saveOption = JOptionPane.showConfirmDialog(this, resourceBundle.getString("file.already.exists.overwrite.it"), resourceBundle.getString("file.already.exists"), JOptionPane.YES_NO_OPTION);
                }
                if (saveOption == JOptionPane.YES_OPTION) {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(this.txtAreaScriptResult.getText().getBytes());
                    fos.close();
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
