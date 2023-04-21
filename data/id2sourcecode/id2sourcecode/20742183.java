    private void export() {
        boolean status;
        boolean already = false;
        final List<String> exportDirList = new ArrayList<String>();
        final AbstractImpExp exportFormat = selectedExportFormat();
        final String exportDir = txtDirectory.getText().trim();
        final ListModel model = lstHists.getModel();
        AbstractHistogram[] exportHistograms = new AbstractHistogram[model.getSize()];
        File[] exportFiles = new File[model.getSize()];
        for (int i = 0; i < exportHistograms.length; i++) {
            final jam.data.AbstractHist1D hist1D = (jam.data.AbstractHist1D) model.getElementAt(i);
            exportHistograms[i] = hist1D;
            final String groupName = hist1D.getGroupName();
            final String histName = hist1D.getName().trim();
            exportFiles[i] = createExportFile(exportDir, groupName, histName, exportFormat.getDefaultExtension());
            final String exportGroupDir = exportDir + File.separator + groupName;
            if (!exportDirList.contains(exportGroupDir)) {
                exportDirList.add(exportGroupDir);
            }
            already |= exportFiles[i].exists();
        }
        status = true;
        status = createExportDir(exportDir);
        status = checkFileOverwrite(status, already);
        if (status) {
            for (int i = 0; i < exportDirList.size(); i++) {
                final boolean statusTemp = createExportDir(exportDirList.get(i));
                status = status && statusTemp;
            }
        }
        if (status) {
            LOGGER.info("Exporting to " + exportDir + ": ");
            for (int i = 0; i < exportFiles.length; i++) {
                LOGGER.info("\t" + exportFiles[i].getName());
                try {
                    exportFormat.saveFile(exportFiles[i], exportHistograms[i]);
                } catch (ImpExpException e) {
                    LOGGER.log(Level.SEVERE, "Exporting file: " + exportFiles[i].getPath() + " " + e.getMessage(), e);
                }
            }
            LOGGER.info("Exporting complete.");
        }
    }
