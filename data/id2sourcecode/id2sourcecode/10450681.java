    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            fileChooserDialog.resetChoosableFileFilters();
            fileChooserDialog.setFileFilter(xmlFilter);
            fileChooserDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooserDialog.setDialogType(JFileChooser.SAVE_DIALOG);
            fileChooserDialog.setDialogTitle("Save As Operation File");
            fileChooserDialog.setSelectedFile(new File(""));
            fileChooserDialog.setCurrentDirectory(new File(marla.ide.gui.Domain.lastGoodDir));
            int response = fileChooserDialog.showSaveDialog(Domain.getTopWindow());
            while (response == JFileChooser.APPROVE_OPTION) {
                int lastIndex = fileChooserDialog.getSelectedFile().toString().lastIndexOf(".");
                if (lastIndex == -1) {
                    lastIndex = fileChooserDialog.getSelectedFile().toString().length();
                    fileChooserDialog.setSelectedFile(new File(fileChooserDialog.getSelectedFile().toString().substring(0, lastIndex) + ".xml"));
                }
                File file = fileChooserDialog.getSelectedFile();
                if (!file.toString().endsWith(".xml")) {
                    marla.ide.gui.Domain.showWarningDialog(Domain.getTopWindow(), "The file must have a valid XML extension.", "Does Not Exist");
                    response = fileChooserDialog.showSaveDialog(Domain.getTopWindow());
                    continue;
                } else if (file.exists()) {
                    response = marla.ide.gui.Domain.showConfirmDialog(Domain.getTopWindow(), "The specified file already exists.  Overwrite?", "Overwrite File", JOptionPane.YES_NO_OPTION);
                    if (response != JOptionPane.YES_OPTION) {
                        response = fileChooserDialog.showOpenDialog(Domain.getTopWindow());
                        continue;
                    }
                }
                currentFile = new OperationFile(currentFile);
                currentFile.setFilePath(file.toString());
                save();
                if (file.isDirectory()) {
                    marla.ide.gui.Domain.lastGoodDir = file.toString();
                } else {
                    marla.ide.gui.Domain.lastGoodDir = file.toString().substring(0, file.toString().lastIndexOf(File.separatorChar));
                }
                openFile();
                break;
            }
        } catch (OperationEditorException ex) {
            Domain.logger.add(ex);
        }
    }
