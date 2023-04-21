    public synchronized void getModulesInfoFromNetwork() {
        final IndeterminateProgressBar downloadListProgress = new IndeterminateProgressBar("Loading information from network...");
        downloadListProgress.setLocationRelativeTo(frame);
        networkModulesModel.clear();
        if (!moduleListLoaded) {
            downloadListProgress.start();
            try {
                GetFileTool gft = new GetFileTool();
                gft.setValidate(false);
                gft.setHash(BigHash.createHashFromString(ConfigureTrancheGUI.get(ConfigureTrancheGUI.CATEGORY_GUI, ConfigureTrancheGUI.PROP_MODULE_LIST_HASH)));
                gft.setSaveFile(ModulePanel.moduleListFile);
                gft.getFile();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                try {
                    URL url = new URL(ConfigureTrancheGUI.get(ConfigureTrancheGUI.CATEGORY_GUI, ConfigureTrancheGUI.PROP_MODULE_LIST_URL));
                    IOUtil.setBytes(IOUtil.getBytes(url.openStream()), ModulePanel.moduleListFile);
                } catch (Exception f) {
                    if (!ModulePanel.moduleListFile.exists()) {
                        ErrorFrame ef = new ErrorFrame();
                        ef.show(e, frame);
                        return;
                    }
                }
            } finally {
                moduleListLoaded = true;
                downloadListProgress.stop();
            }
        }
        InputStream in = null;
        try {
            URL moduleList = ModulePanel.moduleListFile.toURI().toURL();
            try {
                in = moduleList.openStream();
            } catch (IOException ex) {
                installFromServerLabel.setText("Module server down");
                installFromServerDescription.setText("Unfortunately, the module server is temporarily down. Sorry for the inconvenience, but feel free to contact us.");
                return;
            }
            String filename, title, description, hashString;
            filename = getNextLineFromList(in);
            while (filename != null) {
                title = getNextLineFromList(in);
                description = getNextLineFromList(in);
                hashString = getNextLineFromList(in);
                boolean isHash = false;
                try {
                    BigHash.createHashFromString(hashString);
                    isHash = true;
                } catch (Exception ex) {
                }
                if (isHash) {
                    String[] row = { title, description, filename, hashString };
                    if (TrancheModulesUtil.getModuleByName(title) == null) {
                        networkModulesModel.addRow(row);
                    }
                }
                filename = getNextLineFromList(in);
            }
        } catch (Exception ex) {
            ErrorFrame ef = new ErrorFrame();
            ef.show(ex, ModulePanel.this.getParent());
        } finally {
            IOUtil.safeClose(in);
            Thread t = new Thread("Update network modules table thread.") {

                public void run() {
                    networkModulesModel.fireTableDataChanged();
                }
            };
            SwingUtilities.invokeLater(t);
        }
    }
