    public XMLOperatorDocBundle(URL url, String resourceName) throws IOException {
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
        } catch (SAXException e) {
            throw new IOException("Malformed XML operator help bundle: " + e, e);
        } catch (ParserConfigurationException e) {
            LogService.getRoot().log(Level.WARNING, "Cannot create XML parser: " + e, e);
            return;
        }
        NodeList helpElements = document.getDocumentElement().getElementsByTagName("operator");
        for (int i = 0; i < helpElements.getLength(); i++) {
            Element element = (Element) helpElements.item(i);
            OperatorDocumentation operatorDocumentation = new OperatorDocumentation(this, element);
            try {
                String operatorKey = XMLTools.getTagContents(element, "key", false);
                if (operatorKey == null) {
                    operatorKey = XMLTools.getTagContents(element, "name", true);
                    LogService.getRoot().fine("Operator help is missing <key> tag. Using <name> as <key>: " + operatorKey);
                }
                addOperatorDoc(operatorKey, operatorDocumentation);
            } catch (XMLException e) {
                LogService.getRoot().log(Level.WARNING, "Malformed operoator documentation: " + e, e);
            }
        }
        NodeList groupElements = document.getDocumentElement().getElementsByTagName("group");
        for (int i = 0; i < groupElements.getLength(); i++) {
            Element element = (Element) groupElements.item(i);
            GroupDocumentation doc = new GroupDocumentation(element);
            addGroupDoc(doc.getKey(), doc);
        }
        LogService.getRoot().fine("Loaded documentation for " + helpElements.getLength() + " operators and " + groupElements.getLength() + " groups.");
    }
