    public Connection createConnection(String serviceName) throws VCHException {
        try {
            HttpRequest request = httpClient.createRequest(HttpRequest.Method.POST, "/services/" + serviceName);
            HttpResponse response = request.execute();
            switch(response.getStatusCode()) {
                case HttpConstants.StatusCodes.CREATED:
                    String location = Util.getRequiredHeader(response, HttpConstants.Headers.LOCATION);
                    String path = httpClient.getPath(location);
                    if (path == null) {
                        throw new VCHProtocolException("The server returned an unexpected value for the Location header (" + location + "): the location identified by the URL is on a different server.");
                    }
                    if (!path.startsWith("/connections/")) {
                        throw new VCHProtocolException("The server returned a location (" + location + ") that doesn't conform to the VC/H specification");
                    }
                    String connectionId = path.substring(13);
                    if (!isValidConnectionId(connectionId)) {
                        throw new VCHProtocolException("The server returned an invalid connection ID (" + connectionId + ")");
                    }
                    return new ConnectionImpl(httpClient, connectionId);
                case HttpConstants.StatusCodes.NOT_FOUND:
                    throw new NoSuchServiceException(serviceName);
                default:
                    throw Util.createException(response);
            }
        } catch (HttpException ex) {
            throw new VCHConnectionException(ex);
        }
    }
