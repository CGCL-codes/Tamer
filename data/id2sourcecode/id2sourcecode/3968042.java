    public Document read(URL url) throws DocumentException, IOException, XmlPullParserException {
        String systemID = url.toExternalForm();
        return read(createReader(url.openStream()), systemID);
    }
