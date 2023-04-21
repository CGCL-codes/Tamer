    public static void loadConfig(String aConfigLoc) {
        logger.debug("loadConfig() " + aConfigLoc);
        InputStream is = null;
        try {
            URL url = SdlUtil.findOnClasspath(aConfigLoc, AxisEngineConfiguration.class);
            is = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[4096];
            int count = 0;
            while ((count = is.read(buff)) != -1) {
                baos.write(buff, 0, count);
            }
            sConfig = baos.toByteArray();
        } catch (Exception e) {
            logger.error("Error loading Axis client engine configuration (client-config.wsdd).");
            SdlException.logError(e, "Error loading Axis client engine configuration (client-config.wsdd).");
        } finally {
            SdlCloser.close(is);
        }
    }
