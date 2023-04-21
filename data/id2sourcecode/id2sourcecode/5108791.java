    public static Catalog find(String url, final boolean validate) throws SAXException {
        Catalog catalog;
        if ((catalog = catalogs.get(url)) == null) {
            try {
                SAXParser parser = new SAXParser(validate, true, validate, false, null, null);
                CatalogHandler handler = new CatalogHandler(url);
                parser.parse(new InputSource(Application.openStream(url)), handler);
                catalog = handler.getCatalog();
            } catch (ParserConfigurationException error) {
                logger.log(Level.SEVERE, "No suitable JAXP implementation installed", error);
                System.exit(2);
            } catch (IOException error) {
                throw new SAXException("I/O problem whilst parsing XML catalog", error);
            }
            catalogs.put(url, catalog);
        }
        return (catalog);
    }
