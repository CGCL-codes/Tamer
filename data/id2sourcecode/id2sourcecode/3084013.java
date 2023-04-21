    public synchronized Node parse(URL url) throws IOException, SAXException {
        Node n = null;
        InputStream is = url.openStream();
        try {
            n = parse(is);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
            }
        }
        return n;
    }
