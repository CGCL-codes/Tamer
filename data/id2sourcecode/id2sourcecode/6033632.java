    public Config readXML(URL url) {
        Element root = null;
        SAXBuilder parser = new SAXBuilder();
        try {
            Document doc = parser.build(url.openStream());
            root = doc.getRootElement();
        } catch (Exception e) {
            System.out.println("Erro on JDOMReader.readXML: " + e.getMessage());
        }
        return performRead(root);
    }
