    public static MTP loadMTPFromURL(Bundle bundle, URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
            IXMLReader reader = new StdXMLReader(in);
            parser.setReader(reader);
            XMLElement el = (XMLElement) parser.parse();
            return loadMTP(bundle, url, el);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to load " + url + " " + e);
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {
            }
        }
    }
