    private Object loadXml(String property) throws IOException {
        URL url = new URL(System.getProperty(property));
        InputStream is = url.openConnection().getInputStream();
        return xstream.fromXML(is);
    }
