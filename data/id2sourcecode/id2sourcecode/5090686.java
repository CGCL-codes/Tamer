    private InputStream getXSLStream(String xslID) throws IOException {
        InputStream stream = null;
        if (JTradeProperties.INSTANCE.isUserSpecific(xslID)) {
            File xsl = new File(JTradeProperties.INSTANCE.getHome() + File.separator + JTradeProperties.INSTANCE.getProperty(xslID));
            if (xsl.exists()) {
                stream = new FileInputStream(xsl);
            } else {
                URL url = ClassLoader.getSystemResource(JTradeProperties.INSTANCE.getProperty(xslID));
                stream = url.openStream();
            }
        } else {
            URL url = ClassLoader.getSystemResource(JTradeProperties.INSTANCE.getProperty(xslID));
            stream = url.openStream();
        }
        return stream;
    }
