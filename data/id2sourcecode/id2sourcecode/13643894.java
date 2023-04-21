    protected String readAction() throws RequestCancelledException, ChangesOnServerException, ConnectionException {
        GetPageRequest request = getHttpClient().createGetPageRequest();
        request.setUrl("http://www.glowfoto.com");
        HttpResponse response = executeRequest(request);
        try {
            Parser parser = new Parser(response.getResponseBody());
            String action = parser.parseOne("enctype=\"multipart/form-data\" method=\"post\" action=\"(.*)\" style");
            closeReponse(response);
            return action;
        } catch (ParsingException ex) {
            throw new ChangesOnServerException();
        } catch (IOException ex) {
            throw new ChangesOnServerException();
        }
    }
