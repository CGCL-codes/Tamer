    protected void quarantine() {
        log.trace("quarantine()");
        try {
            File quarantineDir = new File(quarantineFolder);
            if (!quarantineDir.exists()) {
                quarantineDir.mkdirs();
            }
            FileUtils.copyFile(new File(attachFilePath), new File(quarantineFolder + "/" + FilenameUtils.getName(attachFilePath)));
            quarantined = true;
        } catch (IOException e) {
            log.error("Failed to quarantine : '" + attachFilePath + "'", e);
        }
    }
