    public static void testSearch() throws Exception {
        URL url = new URL("http://dl.tufts.edu:8080/" + SEARCH_STRING + URLEncoder.encode("street", "ISO-8859-1"));
        XPathFactory factory = XPathFactory.newInstance();
        XPath xPath = factory.newXPath();
        xPath.setNamespaceContext(new FedoraNamespaceContext());
        InputSource inputSource = new InputSource(url.openStream());
        XPathExpression xSession = xPath.compile("//pre:expirationDate/text()");
        String date = xSession.evaluate(inputSource);
        System.out.println("Expiration Date:" + date);
        inputSource = new InputSource(url.openStream());
        NodeList fieldNode = (NodeList) xPath.evaluate("/pre:result/pre:resultList/pre:objectFields", inputSource, XPathConstants.NODESET);
        for (int i = 0; i < fieldNode.getLength(); i++) {
            Node n = fieldNode.item(i);
            System.out.println(i + "name:" + n.getNodeName() + "value: " + n.getNodeValue() + " Type: " + n.getNodeType());
            for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                org.w3c.dom.Node e = n.getChildNodes().item(j);
                if (e.getNodeType() == Node.ELEMENT_NODE) {
                    System.out.println("Name: " + e.getNodeName() + " value: " + e.getFirstChild().getNodeValue() + " Type: " + e.getNodeType());
                }
            }
        }
    }
