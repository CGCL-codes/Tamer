    protected void saveAs() {
        if (problem != null) {
            viewPanel.fileChooserDialog.setDialogTitle("Save Problem As");
            viewPanel.fileChooserDialog.setDialogType(JFileChooser.SAVE_DIALOG);
            viewPanel.fileChooserDialog.resetChoosableFileFilters();
            viewPanel.fileChooserDialog.setFileFilter(marlaFilter);
            viewPanel.fileChooserDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
            viewPanel.fileChooserDialog.setCurrentDirectory(new File(problem.getFileName()));
            viewPanel.fileChooserDialog.setSelectedFile(new File(problem.getFileName()));
            int response = viewPanel.fileChooserDialog.showSaveDialog(Domain.getTopWindow());
            while (response == JFileChooser.APPROVE_OPTION) {
                File file = viewPanel.fileChooserDialog.getSelectedFile();
                if (file.getName().indexOf(".") == -1) {
                    file = new File(viewPanel.fileChooserDialog.getSelectedFile().toString() + ".marla");
                }
                if (!file.toString().toLowerCase().endsWith(".marla")) {
                    Domain.showWarningDialog(Domain.getTopWindow(), "The extension for the file must be .marla.", "Invalid Extension");
                    viewPanel.fileChooserDialog.setSelectedFile(new File(viewPanel.fileChooserDialog.getSelectedFile().toString().substring(0, viewPanel.fileChooserDialog.getSelectedFile().toString().lastIndexOf(".")) + ".marla"));
                    response = viewPanel.fileChooserDialog.showSaveDialog(Domain.getTopWindow());
                    continue;
                }
                boolean continueAllowed = true;
                if (file.exists()) {
                    response = Domain.showConfirmDialog(Domain.getTopWindow(), "The selected file already exists.\nWould you like to overwrite the existing file?", "Overwrite Existing File", JOptionPane.YES_NO_OPTION);
                    if (response != JOptionPane.YES_OPTION) {
                        continueAllowed = false;
                    }
                }
                if (continueAllowed) {
                    problem.setFileName(file.toString());
                    viewPanel.mainFrame.setTitle(viewPanel.mainFrame.getDefaultTitle() + " - " + problem.getFileName().substring(problem.getFileName().lastIndexOf(System.getProperty("file.separator")) + 1, problem.getFileName().lastIndexOf(".")));
                    save();
                    break;
                } else {
                    continue;
                }
            }
        }
    }
