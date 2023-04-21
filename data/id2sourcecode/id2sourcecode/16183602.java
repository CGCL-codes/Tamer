    public static Mapping parseMapping(URL url) throws ParserException, InvalidXMLException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(url.openStream());
            Element mapElement = doc.getDocumentElement();
            Mapping mapping = new MappingImpl();
            mapping.setSourceOntologyURI(URI.create(mapElement.lookupNamespaceURI("source")));
            mapping.setTargetOntologyURI(URI.create(mapElement.lookupNamespaceURI("target")));
            List<Rule> scope = new ArrayList<Rule>();
            List<Element> ruleElements = extractElements(mapElement.getChildNodes(), "Rule");
            if (ruleElements.size() == 0) {
                throw new InvalidXMLException("Zero Rule elements in this document");
            }
            for (Element element : ruleElements) {
                mapping.addRule(parseRule(scope, element));
            }
            return mapping;
        } catch (IOException e) {
            throw new ParserException(e);
        } catch (ParserConfigurationException e) {
            throw new ParserException(e);
        } catch (SAXException e) {
            throw new ParserException(e);
        }
    }
