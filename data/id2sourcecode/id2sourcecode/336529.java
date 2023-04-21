    private void buildAncestorMap() throws ParserConfigurationException, XPathExpressionException {
        int progressCount = 0;
        DocumentBuilder builder = factory.newDocumentBuilder();
        XPath xpath = xfactory.newXPath();
        XPathExpression expr = xpath.compile("//classBean/id");
        XPathExpression levelExpr = xpath.compile("//classBean/relations/entry/int");
        Set<String> conceptNames = conceptMap.keySet();
        for (String conceptName : conceptNames) {
            progressCount += 1;
            if (progressCount % 1000 == 0) {
                System.out.println(progressCount);
            }
            String queryUrl = PARENT_URL + ONTOLOGY_VERSION + "/" + conceptName.replace(" ", "_") + COMMON_SUFFIX;
            try {
                org.w3c.dom.Document doc = builder.parse(queryUrl);
                Object result = expr.evaluate(doc, XPathConstants.NODESET);
                NodeList nodes = (NodeList) result;
                Object levelResult = levelExpr.evaluate(doc, XPathConstants.NODESET);
                NodeList levelNodes = (NodeList) levelResult;
                String conceptCui = conceptMap.get(conceptName);
                Set<String> ancestorCuisAndLevels = ancestorMap.get(conceptCui);
                if (ancestorCuisAndLevels == null) ancestorCuisAndLevels = new HashSet<String>();
                for (int i = 0, len = nodes.getLength(); i < len; i++) {
                    String name = nodes.item(i).getFirstChild().getNodeValue().replace("_", " ");
                    String ancestorCui = conceptMap.get(name);
                    if (ancestorCui == null) continue;
                    String level = levelNodes.item(i).getFirstChild().getNodeValue();
                    ancestorCuisAndLevels.add(ancestorCui + ":" + level);
                }
                ancestorMap.put(conceptCui, ancestorCuisAndLevels);
            } catch (IOException io) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
