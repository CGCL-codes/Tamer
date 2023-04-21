    protected void browseForFileName() {
        final String fileExtension;
        final String formatDescription;
        SelectFormatPage formatPage = (SelectFormatPage) getWizard().getPage("SelectFormatPage");
        if (formatPage != null) {
            fileExtension = formatPage.getSelectedExportFormat().getFormatFileExtension();
            formatDescription = formatPage.getSelectedExportFormat().getFormatDescription();
        } else {
            fileExtension = "";
            formatDescription = "";
        }
        if (prevFileFilter != null) {
            chooser.removeChoosableFileFilter(prevFileFilter);
        }
        chooser.setFileFilter(prevFileFilter = new FileFilter() {

            /**
			 * Whether the given file is accepted by this filter.
			 */
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                } else if (f.getName().endsWith("." + fileExtension)) {
                    return true;
                } else {
                    return false;
                }
            }

            /**
			 * The description of this filter. For example: "JPG and GIF Images"
			 *
			 * @see javax.swing.filechooser.FileView#getName
			 */
            public String getDescription() {
                return formatDescription;
            }
        });
        if (chooser.showSaveDialog(getWizard()) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file.exists() == true) {
                int ret = JOptionPane.showConfirmDialog((Component) getWizard(), "Warning: The selected file already exists.  Do " + "you wish to overwrite it?", "File Exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (ret == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            String pathName = chooser.getSelectedFile().getPath();
            if (pathName.endsWith("." + fileExtension) == false) {
                pathName = pathName + "." + fileExtension;
            }
            pathNameField.setText(pathName);
            setWizardButtonState();
        }
    }
