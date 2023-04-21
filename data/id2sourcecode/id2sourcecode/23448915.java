    protected T fetchFromNetwork(Query query) throws GadgetException {
        HttpRequest request = new HttpRequest(query.specUri).setIgnoreCache(query.ignoreCache).setGadget(query.gadgetUri).setContainer(query.container);
        request.setCacheTtl((int) (refresh / 1000));
        HttpResponse response = pipeline.execute(request);
        if (response.getHttpStatusCode() != HttpResponse.SC_OK) {
            throw new GadgetException(GadgetException.Code.FAILED_TO_RETRIEVE_CONTENT, "Unable to retrieve spec for " + query.specUri + ". HTTP error " + response.getHttpStatusCode());
        }
        try {
            String content = response.getResponseAsString();
            return parse(content, query);
        } catch (XmlException e) {
            throw new SpecParserException(e);
        }
    }
