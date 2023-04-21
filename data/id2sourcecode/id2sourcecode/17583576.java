    public static long getWebXmlLastModified(ExternalContext context) {
        try {
            URL url = context.getResource(WEB_XML_PATH);
            if (url != null) return url.openConnection().getLastModified();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Could not find web.xml in path " + WEB_XML_PATH);
        }
        return 0L;
    }
