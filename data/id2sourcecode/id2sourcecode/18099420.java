    public void checkForUpdate(boolean verbose) {
        try {
            URL url = new URL(VERSION_FILE_URL);
            urlc = (HttpURLConnection) url.openConnection();
            urlc.setAllowUserInteraction(false);
            urlc.setRequestMethod("GET");
            urlc.setDoInput(true);
            urlc.setDoOutput(false);
            urlc.connect();
            propertyInputStream = urlc.getInputStream();
            Properties properties = new Properties();
            properties.load(propertyInputStream);
            versionPropertyString = properties.getProperty("app.version");
            ArchitectVersion latestVersion = new ArchitectVersion(versionPropertyString);
            ArchitectVersion userVersion = ArchitectVersion.APP_FULL_VERSION;
            if (userVersion.compareTo(latestVersion) < 0) {
                promptUpdate();
            } else if (verbose) {
                JOptionPane.showMessageDialog(getSession().getArchitectFrame(), Messages.getString("CheckForUpdateAction.upToDate"), Messages.getString("CheckForUpdateAction.name"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            logger.error("Fail to retrieve current version number!");
            if (verbose) {
                ASUtils.showExceptionDialogNoReport(getSession().getArchitectFrame(), Messages.getString("CheckForUpdateAction.failedToCheckForUpdate"), ex);
            }
        } finally {
            urlc.disconnect();
            try {
                if (propertyInputStream != null) {
                    propertyInputStream.close();
                }
            } catch (IOException ex2) {
                logger.error("Exception while trying to close input stream.");
                throw new RuntimeException(ex2);
            }
        }
    }
