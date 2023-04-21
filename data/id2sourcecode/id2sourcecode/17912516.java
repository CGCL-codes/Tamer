    private static void initializeDistribModeConfigFiles() {
        Log.log(Log.NOTICE, PluginHome.class, "Initializing distrib config files.");
        for (String modeConfigName : distribModeConfigNames) {
            File modeConfigFile = getModeConfigFile(modeConfigName);
            if (modeConfigFile.exists()) continue;
            InputStream is = getDistribModeConfig(modeConfigName);
            if (is == null) throw new AssertionError("distribution configuration for mode not found: " + modeConfigName);
            try {
                is = new BufferedInputStream(is);
                modeConfigFile.createNewFile();
                OutputStream os = new BufferedOutputStream(new FileOutputStream(modeConfigFile));
                for (int r; (r = is.read()) != -1; ) os.write(r);
                is.close();
                os.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
