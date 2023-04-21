    private Policy(URL url) throws PolicyException {
        try {
            InputSource source = resolveEntity(null, url.toExternalForm());
            if (source == null) {
                source = new InputSource(url.toExternalForm());
                source.setByteStream(url.openStream());
            } else {
                source.setSystemId(url.toExternalForm());
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = null;
            dom = db.parse(source);
            Element topLevelElement = dom.getDocumentElement();
            NodeList includes = topLevelElement.getElementsByTagName("include");
            for (int i = 0; i < includes.getLength(); i++) {
                Element include = (Element) includes.item(i);
                String href = XMLUtil.getAttributeValue(include, "href");
                Element includedPolicy = getPolicy(href);
                parsePolicy(includedPolicy);
            }
            parsePolicy(topLevelElement);
        } catch (SAXException e) {
            throw new PolicyException(e);
        } catch (ParserConfigurationException e) {
            throw new PolicyException(e);
        } catch (IOException e) {
            throw new PolicyException(e);
        }
    }
