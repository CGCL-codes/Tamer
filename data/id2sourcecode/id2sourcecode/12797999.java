    private void createFont(URL url) throws FOPException {
        XMLReader parser = ConfigurationReader.createParser();
        if (parser == null) throw new FOPException("Unable to create SAX parser");
        try {
            parser.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
        } catch (SAXException e) {
            throw new FOPException("You need a SAX parser which supports SAX version 2", e);
        }
        parser.setContentHandler(this);
        try {
            parser.parse(new InputSource(url.openStream()));
        } catch (SAXException e) {
            throw new FOPException(e);
        } catch (IOException e) {
            throw new FOPException(e);
        }
    }
