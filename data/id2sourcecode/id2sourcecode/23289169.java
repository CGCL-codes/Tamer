    public Config() {
        String resource = "hula_config.xml";
        URL url = ClassLoader.getSystemClassLoader().getResource(resource);
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(resource);
        }
        if (url != null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setNamespaceAware(false);
            try {
                DocumentBuilder builder = dbf.newDocumentBuilder();
                builder.setErrorHandler(new SimpleErrorHandler());
                load(builder.parse(url.openStream()));
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }
