    public static void boot() throws JGCSException {
        if (booted) return;
        services = new Properties();
        try {
            URL props_url = AppiaServiceList.class.getResource(PROPS_FILE);
            if (props_url == null) throw new IOException("Could not find properties file: " + PROPS_FILE);
            InputStream is = props_url.openStream();
            services.load(is);
            is.close();
        } catch (IOException e) {
            throw new JGCSException("Unable to boot the Services List.", e);
        }
    }
