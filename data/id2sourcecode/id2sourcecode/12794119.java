    protected void reloadConfig() {
        String config = System.getProperty(CONFIG_FILE);
        if (config == null || config.equals("")) {
            config = DEFAULT_CONFIG_FILE;
        }
        if (new File(config).exists()) {
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(config));
                propFile.load(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL url = Thread.currentThread().getContextClassLoader().getResource(config);
                if (url != null) {
                    InputStream inputStream = url.openStream();
                    propFile.load(inputStream);
                    inputStream.close();
                }
            } catch (Exception e) {
            }
        }
    }
