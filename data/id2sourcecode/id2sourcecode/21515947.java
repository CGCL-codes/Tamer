    @Override
    public void sync(String templateId, String language, String type) {
        for (int i = 0; i < EXTS.length; i++) {
            final String ext = EXTS[i];
            final File from = getLocalFile(templateId + ext, language, type);
            final File to = getCloudFile(templateId + ext, language, type);
            try {
                if (from.exists()) {
                    ComptaPropsConfiguration config = ComptaPropsConfiguration.getInstanceCompta();
                    SyncClient c = createSyncClient(config);
                    String remotePath = "Template/";
                    if (language != null && language.trim().length() > 0) {
                        remotePath += language + "/";
                    }
                    remotePath += idSociete;
                    System.out.println("Sending on cloud:" + from.getCanonicalPath() + " to " + remotePath + " " + from.getName());
                    c.sendFile(from, remotePath, from.getName(), config.getToken());
                    FileUtils.copyFile(from, to);
                    ensureDelete(from);
                }
            } catch (Exception e) {
                throw new IllegalStateException("Synchronisation impossible", e);
            }
        }
    }
