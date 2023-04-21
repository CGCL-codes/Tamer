    public static TagLibrary create(URL url) throws IOException {
        InputStream is = null;
        TagLibrary t = null;
        try {
            is = url.openStream();
            LibraryHandler handler = new LibraryHandler(url);
            SAXParser parser = createSAXParser(handler);
            parser.parse(is, handler);
            t = handler.getLibrary();
        } catch (SAXException e) {
            IOException ioe = new IOException("Error parsing [" + url + "]: ");
            ioe.initCause(e);
            throw ioe;
        } catch (ParserConfigurationException e) {
            IOException ioe = new IOException("Error parsing [" + url + "]: ");
            ioe.initCause(e);
            throw ioe;
        } finally {
            if (is != null) is.close();
        }
        return t;
    }
