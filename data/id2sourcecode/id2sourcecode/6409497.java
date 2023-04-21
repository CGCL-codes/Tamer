    private void export(boolean pdf) {
        UIPropertyContext context = UIPropertyContext.createContext("ExportDialog");
        final JFileChooser fcExport = new JFileChooser();
        fcExport.setFileSelectionMode(JFileChooser.FILES_ONLY);
        File baseDir = null;
        {
            String path;
            if (pdf) {
                path = context.getProperty(PDF_EXPORT_DIR_PROP);
            } else {
                path = context.getProperty(HTML_EXPORT_DIR_PROP);
            }
            if (path != null) {
                baseDir = new File(path);
            }
        }
        if (baseDir == null || !baseDir.isDirectory()) {
            baseDir = SystemUtils.getUserHome();
        }
        fcExport.setCurrentDirectory(baseDir);
        URI uri = (URI) fileList.getSelectedValue();
        String extension = FilenameUtils.getExtension(uri.toString());
        if (pdf) {
            fcExport.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents (*.pdf)", "pdf"));
        } else if ("htm".equalsIgnoreCase(extension) || "html".equalsIgnoreCase(extension)) {
            fcExport.addChoosableFileFilter(new FileNameExtensionFilter("HTML Documents (*.htm, *.html)", "htm", "html"));
        } else if ("xml".equalsIgnoreCase(extension)) {
            fcExport.addChoosableFileFilter(new FileNameExtensionFilter("XML Documents (*.xml)", "xml"));
        } else {
            String desc = extension + " Files (*." + extension + ")";
            fcExport.addChoosableFileFilter(new FileNameExtensionFilter(desc, extension));
        }
        String name;
        File path;
        if (!partyBox.isSelected()) {
            CharacterFacade character = (CharacterFacade) characterBox.getSelectedItem();
            path = character.getFileRef().getReference();
            if (path != null) {
                path = path.getParentFile();
            } else {
                path = new File(PCGenSettings.getPcgDir());
            }
            name = character.getNameRef().getReference();
        } else {
            path = new File(PCGenSettings.getPcgDir());
            name = "Entire Party";
        }
        if (pdf) {
            fcExport.setSelectedFile(new File(path, name + ".pdf"));
        } else {
            fcExport.setSelectedFile(new File(path, name + "." + extension));
        }
        fcExport.setDialogTitle("Export " + name);
        if (fcExport.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        final File outFile = fcExport.getSelectedFile();
        if (pdf) {
            context.setProperty(PDF_EXPORT_DIR_PROP, outFile.getParent());
        } else {
            context.setProperty(HTML_EXPORT_DIR_PROP, outFile.getParent());
        }
        if (StringUtils.isEmpty(outFile.getName())) {
            pcgenFrame.showErrorMessage("PCGen", "You must set a filename.");
            return;
        }
        if (outFile.isDirectory()) {
            pcgenFrame.showErrorMessage("PCGen", "You cannot overwrite a directory with a file.");
            return;
        }
        if (outFile.exists() && SettingsHandler.getAlwaysOverwrite() == false) {
            int reallyClose = JOptionPane.showConfirmDialog(this, "The file " + outFile.getName() + " already exists, are you sure you want to overwrite it?", "Confirm overwriting " + outFile.getName(), JOptionPane.YES_NO_OPTION);
            if (reallyClose != JOptionPane.YES_OPTION) {
                return;
            }
        }
        try {
            if (pdf) {
                new PDFExporter(outFile, extension, name).execute();
            } else {
                printToFile(outFile);
                maybeOpenFile(outFile);
                Globals.executePostExportCommandStandard(outFile.getAbsolutePath());
            }
        } catch (IOException ex) {
            pcgenFrame.showErrorMessage("PCGen", "Could not export " + name + ". Try another filename.");
            Logging.errorPrint("Could not export " + name, ex);
        }
    }
