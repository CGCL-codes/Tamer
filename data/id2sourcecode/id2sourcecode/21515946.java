    @Override
    public void unSync(String templateId, String language, String type) {
        for (int i = 0; i < EXTS.length; i++) {
            final String ext = EXTS[i];
            final File from = getCloudFile(templateId + ext, language, type);
            final File to = getLocalFile(templateId + ext, language, type);
            try {
                if (from.exists() && !to.exists()) {
                    FileUtils.copyFile(from, to);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Copie impossible", e);
            }
        }
    }
