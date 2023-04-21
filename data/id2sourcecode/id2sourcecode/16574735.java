    protected void initializeTemplates() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        NodeList elements = (NodeList) getXpath().evaluate("/template-index/template", getXmldoc(), XPathConstants.NODESET);
        for (int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);
            NamedNodeMap attr = element.getAttributes();
            Node nameNode = attr.getNamedItem("name");
            String name = nameNode.getNodeValue();
            Node urlNode = attr.getNamedItem("url");
            String urlString = null;
            if (null != urlNode) {
                urlString = getBaseUrl() + urlNode.getNodeValue();
            } else {
                Node absUrlNode = attr.getNamedItem("absurl");
                if (null != absUrlNode) {
                    urlString = absUrlNode.getNodeValue();
                }
            }
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            Template templ = new Template(con.getInputStream(), getBaseUrl());
            if (null != templ) {
                getTemplates().put(name, templ);
            }
        }
    }
