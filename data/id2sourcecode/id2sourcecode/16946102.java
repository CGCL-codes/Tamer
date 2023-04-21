    public void parse(String systemId) throws IOException, SAXException {
        URL url = null;
        try {
            url = new URL(systemId);
        } catch (MalformedURLException e) {
            File file = new File(systemId);
            if (!file.exists()) {
                throw new FileNotFoundException(systemId);
            }
            String path = file.getAbsolutePath();
            if (File.separatorChar != '/') {
                path = path.replace(File.separatorChar, '/');
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (!path.endsWith("/") && file.isDirectory()) {
                path = path + "/";
            }
            url = new URL("file:" + path);
        }
        InputSource source = new InputSource(url.toString());
        source.setByteStream(url.openStream());
        parse(source);
    }
