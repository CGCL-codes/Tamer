    @Test
    public void testWrite() throws DITAOTException, ParserConfigurationException, SAXException, IOException {
        final ConrefPushParser parser = new ConrefPushParser();
        final ConrefPushReader reader = new ConrefPushReader();
        reader.read(inputFile.getAbsolutePath());
        final Set<Map.Entry<String, Hashtable<String, String>>> pushSet = (Set<Map.Entry<String, Hashtable<String, String>>>) reader.getContent().getCollection();
        final Iterator<Map.Entry<String, Hashtable<String, String>>> iter = pushSet.iterator();
        if (iter.hasNext()) {
            final Map.Entry<String, Hashtable<String, String>> entry = iter.next();
            FileUtils.copyFile(new File(srcDir, "conrefpush_stub2_backup.xml"), new File(entry.getKey()));
            final Content content = new ContentImpl();
            content.setValue(entry.getValue());
            parser.setContent(content);
            parser.write(entry.getKey());
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(new File(entry.getKey()));
            final Element elem = document.getDocumentElement();
            NodeList nodeList = elem.getChildNodes();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < nodeList.getLength(); j++) {
                    if (nodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        nodeList = nodeList.item(j).getChildNodes();
                        break;
                    }
                }
            }
            Element element;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    if (element.getAttributes().getNamedItem("id") != null && element.getAttributes().getNamedItem("id").getNodeValue().equals("A")) {
                        node = element.getPreviousSibling();
                        while (node.getNodeType() != Node.ELEMENT_NODE) {
                            node = node.getPreviousSibling();
                        }
                        assertEquals("<li class=\"- topic/li task/step \"><ph class=\"- topic/ph task/cmd \">before</ph></li>", nodeToString((Element) node));
                    } else if (element.getAttributes().getNamedItem("id") != null && element.getAttributes().getNamedItem("id").getNodeValue().equals("B")) {
                        node = element.getNextSibling();
                        while (node.getNodeType() != Node.ELEMENT_NODE) {
                            node = node.getNextSibling();
                        }
                        assertEquals("<li class=\"- topic/li task/step \"><ph class=\"- topic/ph task/cmd \">after</ph></li>", nodeToString((Element) node));
                        node = node.getNextSibling();
                        while (node.getNodeType() != Node.ELEMENT_NODE) {
                            node = node.getNextSibling();
                        }
                        assertEquals("<li class=\"- topic/li task/step \" id=\"C\"><ph class=\"- topic/ph task/cmd \">replace</ph></li>", nodeToString((Element) node));
                    }
                }
            }
        }
    }
