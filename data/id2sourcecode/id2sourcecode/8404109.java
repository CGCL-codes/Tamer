    public void init() {
        try {
            prefilledCacheManager = new Properties();
            URL url = SpriteStore.get().getResourceURL("cache/midhedava.cache");
            if (url != null) {
                InputStream is = url.openStream();
                prefilledCacheManager.load(is);
                is.close();
            }
            if (!Debug.WEB_START_SANDBOX) {
                File file = new File(System.getProperty("user.home") + "/" + midhedava.MIDHEDAVA_FOLDER);
                if (!file.exists() && !file.mkdir()) {
                    logger.error("Can't create " + file.getAbsolutePath() + " folder");
                } else if (file.exists() && file.isFile()) {
                    if (!file.delete() || !file.mkdir()) {
                        logger.error("Can't removing file " + file.getAbsolutePath() + " and creating a folder instead.");
                    }
                }
                file = new File(System.getProperty("user.home") + midhedava.MIDHEDAVA_FOLDER + "cache/");
                if (!file.exists() && !file.mkdir()) {
                    logger.error("Can't create " + file.getAbsolutePath() + " folder");
                }
                String cacheFile = System.getProperty("user.home") + midhedava.MIDHEDAVA_FOLDER + "cache/midhedava.cache";
                new File(cacheFile).createNewFile();
            }
            Configuration.setConfigurationFile(true, midhedava.MIDHEDAVA_FOLDER, "cache/midhedava.cache");
            cacheManager = Configuration.getConfiguration();
        } catch (Exception e) {
            logger.error("cannot create Midhedava Client", e);
        }
    }
