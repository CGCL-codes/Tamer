    public void run() {
        try {
            URL url = new URL("http://s3.amazonaws.com/MinecraftResources/");
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
            Document document = documentbuilder.parse(url.openStream());
            NodeList nodelist = document.getElementsByTagName("Contents");
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < nodelist.getLength(); j++) {
                    Node node = nodelist.item(j);
                    if (node.getNodeType() != 1) {
                        continue;
                    }
                    Element element = (Element) node;
                    String s = ((Element) element.getElementsByTagName("Key").item(0)).getChildNodes().item(0).getNodeValue();
                    long l = Long.parseLong(((Element) element.getElementsByTagName("Size").item(0)).getChildNodes().item(0).getNodeValue());
                    if (l <= 0L) {
                        continue;
                    }
                    downloadAndInstallResource(url, s, l, i);
                    if (closing) {
                        return;
                    }
                }
            }
        } catch (Exception exception) {
            loadResource(resourcesFolder, "");
            exception.printStackTrace();
        }
    }
