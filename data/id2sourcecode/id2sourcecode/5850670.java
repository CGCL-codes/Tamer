    public static String getRuntimeConfigDefsAsString() {
        mLogger.debug("Trying to load runtime config defs file");
        try {
            InputStreamReader reader = new InputStreamReader(RollerConfig.class.getResourceAsStream(runtime_config));
            StringWriter configString = new StringWriter();
            char[] buf = new char[8196];
            int length = 0;
            while ((length = reader.read(buf)) > 0) configString.write(buf, 0, length);
            reader.close();
            return configString.toString();
        } catch (Exception e) {
            mLogger.error("Error loading runtime config defs file", e);
        }
        return "";
    }
