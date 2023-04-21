    public void doImportSourceFiles() {
        OmegaTFileChooser chooser = new OmegaTFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setDialogTitle(OStrings.getString("TF_FILE_IMPORT_TITLE"));
        int result = chooser.showOpenDialog(this);
        if (result == OmegaTFileChooser.APPROVE_OPTION) {
            String projectsource = Core.getProject().getProjectProperties().getSourceRoot();
            File sourcedir = new File(projectsource);
            File[] selFiles = chooser.getSelectedFiles();
            try {
                for (int i = 0; i < selFiles.length; i++) {
                    File selSrc = selFiles[i];
                    if (selSrc.isDirectory()) {
                        List<String> files = new ArrayList<String>();
                        StaticUtils.buildFileList(files, selSrc, true);
                        String selSourceParent = selSrc.getParent();
                        for (String filename : files) {
                            String midName = filename.substring(selSourceParent.length());
                            File src = new File(filename);
                            File dest = new File(sourcedir, midName);
                            LFileCopy.copy(src, dest);
                        }
                    } else {
                        File dest = new File(sourcedir, selFiles[i].getName());
                        LFileCopy.copy(selSrc, dest);
                    }
                }
                ProjectUICommands.projectReload();
            } catch (IOException ioe) {
                displayErrorRB(ioe, "MAIN_ERROR_File_Import_Failed");
            }
        }
    }
