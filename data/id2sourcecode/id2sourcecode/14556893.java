    public static org.osid.repository.AssetIterator advancedSearch(Repository repository, SearchCriteria lSearchCriteria) throws org.osid.repository.RepositoryException {
        try {
            String query = getQueryFromConditions(lSearchCriteria.getConditions());
            NodeList fieldNode = null;
            URL url = new URL("http", repository.getAddress(), repository.getPort(), SEARCH_ADVANCED + query);
            System.out.println("Advanced search url: " + url);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            xPath.setNamespaceContext(new FedoraNamespaceContext());
            InputSource inputSource = new InputSource(url.openStream());
            fieldNode = (NodeList) xPath.evaluate("/pre:result/pre:resultList/pre:objectFields", inputSource, XPathConstants.NODESET);
            if (fieldNode.getLength() > 0) {
                inputSource = new InputSource(url.openStream());
                XPathExpression xSession = xPath.compile("//pre:token/text()");
                String token = xSession.evaluate(inputSource);
                lSearchCriteria.setToken(token);
            }
            return getAssetIterator(repository, fieldNode);
        } catch (Throwable t) {
            throw wrappedException("search", t);
        }
    }
