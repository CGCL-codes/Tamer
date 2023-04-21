    private InputSource resolveInputSource(String path) throws IOException {
        try {
            InputSource inputsource = new InputSource();
            URI url = new URI(path);
            InputStream is;
            if (url.isAbsolute()) is = new URL(path).openStream(); else {
                File file = new File(rootURL + path);
                is = new FileInputStream(file);
            }
            inputsource.setByteStream(is);
            inputsource.setSystemId(path);
            return inputsource;
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }
