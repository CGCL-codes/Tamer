    private boolean transformMapWithXslt(String xsltFileName, File saveFile, String areaCode) throws IOException {
        StringWriter writer = getMapXml();
        StringReader reader = new StringReader(writer.getBuffer().toString());
        URL xsltUrl = getResource(xsltFileName);
        if (xsltUrl == null) {
            logger.severe("Can't find " + xsltFileName + " as resource.");
            throw new IllegalArgumentException("Can't find " + xsltFileName + " as resource.");
        }
        InputStream xsltFile = xsltUrl.openStream();
        return transform(new StreamSource(reader), xsltFile, saveFile, areaCode);
    }
