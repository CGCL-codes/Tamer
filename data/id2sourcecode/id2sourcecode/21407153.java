    public void loadModuleFromNetwork() {
        final IndeterminateProgressBar downloadAndInstallProgress = new IndeterminateProgressBar("Downloading and installing module...");
        downloadAndInstallProgress.setLocationRelativeTo(frame);
        File jarFile = null;
        try {
            int row = networkModulesTable.getSelectedRow();
            String jarFilename = (String) networkModulesModel.getValueAt(row, 2);
            jarFile = new File(TrancheModulesUtil.MODULE_DIRECTORY, jarFilename.trim());
            if (jarFile.exists()) {
                int n = GenericOptionPane.showConfirmDialog(frame, "A module with that name already exists. Continue and overwrite?", "Module already exists", JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.NO_OPTION) {
                    return;
                }
            }
            BigHash moduleHash = networkModulesModel.getHashForModule(row);
            downloadAndInstallProgress.start();
            if (true) {
                throw new TodoException();
            }
            try {
                if (!TrancheModulesUtil.MODULE_DIRECTORY.exists()) {
                    TrancheModulesUtil.MODULE_DIRECTORY.mkdirs();
                }
                BigHash hash = moduleHash;
                GetFileTool gft = new GetFileTool();
                gft.setValidate(false);
                gft.setHash(hash);
                gft.setSaveFile(jarFile);
                gft.getFile();
            } catch (Exception ex) {
                ErrorFrame ef = new ErrorFrame();
                ef.show(ex, frame);
                downloadAndInstallProgress.stop();
                return;
            }
            downloadAndInstallProgress.stop();
            try {
                TrancheModulesUtil.reloadModules();
                clearTablesAndShowModules(TrancheModulesUtil.getModules());
                saveModulesToPreferences();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.err);
            }
        } catch (Exception e) {
            downloadAndInstallProgress.stop();
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            GenericOptionPane.showMessageDialog(frame, "The file could not be download at this time. Please try again later.", "File could not be downloaded", JOptionPane.INFORMATION_MESSAGE);
        }
    }
