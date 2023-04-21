    private void addKeystore() {
        try {
            if (!chartsyPreferences.getBoolean("ks.init", false) && getSrcFile().exists()) {
                FileUtils.copyFile(getSrcFile(), getDestFile());
                autoUpdatePreferences.put("userKS", "user.ks");
                autoUpdatePreferences.put("period", "1");
                chartsyPreferences.putBoolean("ks.init", true);
            }
        } catch (IOException ex) {
            LOG.log(Level.INFO, "", ex);
            chartsyPreferences.putBoolean("ks.init", false);
        }
    }
