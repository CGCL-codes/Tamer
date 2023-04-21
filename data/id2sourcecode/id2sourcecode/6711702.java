    public List<USFinancialReportingTermsContext> makeReportingContextsFromStream(InputStream ios) throws XbrlServiceException {
        XmlObject xml = XmlBeansUtils.parseXml(ios);
        ContextElementsQuery elementsQuery = new ContextElementsQuery();
        XmlObject contextsXml = elementsQuery.collectContexts(xml);
        List<Context> clist = (List<Context>) getContextDigester().digest(StringUtils.makeStreamFromString(contextsXml.toString()));
        USFinancialReportingPrimaryTermsElementsQuery xquerySample = new USFinancialReportingPrimaryTermsElementsQuery();
        List<USFinancialReportingTermsElement> allElements = new ArrayList<USFinancialReportingTermsElement>();
        for (String digesterKey : usfrDigesterMap.keySet()) {
            XbrlDigester digester = usfrDigesterMap.get(digesterKey);
            XmlObject xqCollectAllTerms = xquerySample.collectByTerm(xml, digesterKey);
            List<USFinancialReportingTermsElement> digestedElements = (List<USFinancialReportingTermsElement>) digester.digest(StringUtils.makeStreamFromString(xqCollectAllTerms.toString()));
            if (digestedElements != null) allElements.addAll(digestedElements);
        }
        return correlateContextWithElements(clist, allElements);
    }
