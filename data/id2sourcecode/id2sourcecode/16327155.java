    public InputSource resolveEntity(String publicId, String systemId) {
        InputSource r = null;
        logger.fine("Resolver: resolveEntity(\"" + publicId + "\", \"" + systemId + "\")");
        if (systemId.equals("http://doors.sourceforge.net/doors.dtd")) {
            String resourceName = DtdResolver.class.getResource("") + "doors.dtd";
            URL url = DtdResolver.class.getResource("/doors.dtd");
            logger.fine("Resolver: Trying to resolve " + systemId);
            logger.fine("Resolver: Local URL is " + url);
            if (url == null) {
                logger.severe("Resolver: Cannot find doors.dtd as " + resourceName);
            } else {
                try {
                    InputStream is = url.openStream();
                    r = new InputSource(is);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "When trying to open '" + resourceName + "'", e);
                }
            }
        }
        return r;
    }
