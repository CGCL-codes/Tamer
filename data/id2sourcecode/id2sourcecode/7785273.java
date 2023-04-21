    private void initialize(URL url) throws SAXException, IOException {
        Document doc = this.m_builder.parse(url.openStream());
        NodeList plugins = doc.getDocumentElement().getElementsByTagName("Plugin");
        for (int count = 0; count < plugins.getLength(); count++) {
            Element plug = (Element) plugins.item(count);
            String name = plug.getAttribute("name");
            String cl = plug.getAttribute("class");
            String group = plug.getAttribute("group");
            this.m_plugins.put(name, new PluginDescriptor(name, cl, group));
        }
    }
