    private void initAckRules() throws Exception {
        ackRules = new HashMap<Message, Message>();
        final File fRule = new File(ruleFile);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                File dtdFile = new File(fRule.getParentFile(), "ack.dtd");
                if (dtdFile.exists()) {
                    return new InputSource(new FileInputStream(dtdFile));
                }
                URL url = Thread.currentThread().getContextClassLoader().getResource("ack.dtd");
                if (url != null) {
                    return new InputSource(url.openStream());
                }
                return null;
            }
        });
        Document document = builder.parse(fRule);
        Element rootElement = document.getDocumentElement();
        NodeList ruleNodeList = rootElement.getChildNodes();
        for (int i = 0; i < ruleNodeList.getLength(); i++) {
            Node node = ruleNodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element ruleEle = (Element) node;
                NodeList reqList = ruleEle.getElementsByTagName("request");
                NodeList repList = ruleEle.getElementsByTagName("response");
                Element reqestEle = (Element) reqList.item(0);
                Element repsoneEle = (Element) repList.item(0);
                String reqType = reqestEle.getAttribute("type");
                String reqData = reqestEle.getTextContent().trim();
                String repType = repsoneEle.getAttribute("type");
                String repData = repsoneEle.getTextContent().trim();
                Message reqMsg = new Message(reqData, reqType);
                Message repMsg = new Message(repData, repType);
                addRule(reqMsg, repMsg);
            }
        }
    }
