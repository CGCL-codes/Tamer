    @SuppressWarnings("static-access")
    public String saveAs(File selectedFile) throws IOException {
        String savedToFile = null;
        try {
            tmpKeyTab.save();
            FileUtils.copyFile(new File(tmpKeyTab.tabName()), selectedFile);
            savedToFile = selectedFile.getCanonicalPath();
            log.info("Saved file " + savedToFile);
            open(selectedFile);
        } catch (Exception e) {
            log.warning("Couldn't save to file " + selectedFile);
            log.throwing(this.getClass().getCanonicalName(), "saveAs", e);
            String msg = KeytabResources.get().getString("error.keytab.save.error");
            msg.replace("$1", selectedFile.toString());
            sendNotification(MSG_FOR_USER, msg);
        }
        return savedToFile;
    }
