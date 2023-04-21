    @Override
    public void sync(String templateId, String language, String type) {
        for (int i = 0; i < EXTS.length; i++) {
            final String ext = EXTS[i];
            final File from = getLocalFile(templateId + ext, language, type);
            final File to = getFile(templateId + ext, language, type);
            try {
                if (from.exists()) {
                    FileUtils.copyFile(from, to);
                    ensureDelete(from);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Synchronisation impossible", e);
            }
        }
    }
