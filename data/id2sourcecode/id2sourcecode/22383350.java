    public void doRequest() throws PSIException {
        HttpClient client = new DefaultHttpClient();
        try {
            URI uri = new URI(url);
            HttpResponse response = client.execute(builder.buildRequest(uri));
            HttpStatusCode responseCode = HttpStatusCode.getCode(response.getStatusLine().getStatusCode());
            Iterator<ResponseHandler> rhit = handlers.iterator();
            while (rhit.hasNext()) {
                rhit.next().handleResponse(response, responseCode);
            }
        } catch (URISyntaxException use) {
            throw new PSIException(use);
        } catch (IOException ioe) {
            throw new PSIException(ioe);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
