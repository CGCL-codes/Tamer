    public void run() {
        boolean disableZipLog = "true".equals(JSystemProperties.getInstance().getPreference(FrameworkOptions.HTML_ZIP_DISABLE));
        if (disableZipLog || !zipFirst) {
            if (deleteCurrent) {
                deleteLogDirectory();
            }
            return;
        }
        if (JSystemProperties.getInstance().isJsystemRunner()) {
            System.out.println("Log backup process ... (don't close)");
        }
        String date = Summary.getInstance().getProperties().getProperty("Date");
        if (date == null) {
            date = DateUtils.getDate();
            if (date == null) {
                date = Long.toString(System.currentTimeMillis());
            }
        }
        String fileName = "log_" + date.replace(':', '_').replace(' ', '_').replace('+', '_');
        File zipFile = new File(oldDir, fileName + ".zip");
        int index = 1;
        String oFileName = fileName;
        while (zipFile.exists()) {
            fileName = oFileName + "_" + index;
            zipFile = new File(oldDir, fileName + ".zip");
            index++;
        }
        try {
            String[] toDeleteList = toDelete.list();
            if (toDeleteList != null && toDeleteList.length > 0) {
                FileUtils.zipDirectory(toDelete.getPath(), "", zipFile.getPath(), JSystemProperties.getInstance().isJsystemRunner());
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Fail to zip old log - Current logs are not deleted!!!", e);
            return;
        }
        File sutFile = SutFactory.getInstance().getSutFile(false);
        if (sutFile != null) {
            String setup = null;
            setup = sutFile.getName();
            if (setup != null && setup.toLowerCase().endsWith(".xml")) {
                setup = setup.substring(0, setup.length() - 4);
            }
            String oldPath = JSystemProperties.getInstance().getPreference(FrameworkOptions.HTML_OLD_PATH);
            File dest;
            if (oldPath == null) {
                dest = new File(oldDir.getPath() + File.separator + "setup-" + setup + File.separator + "version-" + Summary.getInstance().getProperties().getProperty("Version"));
            } else {
                dest = findTreePath(oldDir, oldPath);
            }
            dest.mkdirs();
            try {
                if (zipFile.exists()) {
                    FileUtils.copyFile(zipFile, new File(dest, fileName + ".zip"));
                }
            } catch (IOException e1) {
                log.log(Level.WARNING, "Fail to copy old log to Hierarchical folders of Sut and Version", e1);
                return;
            }
            String htmlTree = JSystemProperties.getInstance().getPreference(FrameworkOptions.HTML_ZIP_TREE_ONLY);
            if (htmlTree != null && htmlTree.toLowerCase().equals("true")) {
                zipFile.delete();
            }
        } else {
            log.info("Skipped Html zip tree - No Sut!");
        }
        if (deleteCurrent) {
            deleteLogDirectory();
        } else {
            try {
                FileUtils.write(toDelete.getPath() + File.separator + ".zipped", "");
            } catch (IOException e) {
                log.warning("Creating .zip file was failed");
            }
        }
    }
