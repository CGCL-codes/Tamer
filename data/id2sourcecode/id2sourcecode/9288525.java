    public Version getNewestVersion() throws ParserConfigurationException, MalformedURLException, IOException, SAXException {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        URL url = new URL(org.jdesktop.application.Application.getInstance(EDACCApp.class).getContext().getResourceMap(Version.class).getString("version.url"));
        URLConnection con = url.openConnection();
        Document doc = docBuilder.parse(con.getInputStream());
        if (doc.getChildNodes().getLength() != 1 || !"versions".equals(doc.getChildNodes().item(0).getNodeName())) {
            return new Version();
        }
        Node root = doc.getChildNodes().item(0);
        Version new_version = new Version();
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node node = root.getChildNodes().item(i);
            String version = null;
            String v_url = null;
            String md5 = null;
            if ("version".equals(node.getNodeName())) {
                for (int j = 0; j < node.getChildNodes().getLength(); j++) {
                    Node n = node.getChildNodes().item(j);
                    if ("v".equals(n.getNodeName())) {
                        version = n.getTextContent();
                    } else if ("location".equals(n.getNodeName())) {
                        v_url = n.getTextContent();
                    } else if ("md5".equals(n.getNodeName())) {
                        md5 = n.getTextContent();
                    }
                }
                Version v = null;
                try {
                    v = new Version(version, v_url, md5);
                } catch (Exception ex) {
                }
                if (v != null && new_version.compareTo(v) < 0) {
                    new_version = v;
                }
            }
        }
        return new_version;
    }
