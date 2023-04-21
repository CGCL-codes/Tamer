    @WebMethod
    public String get(@WebParam(name = "modelId") final String modelId) throws java.io.IOException {
        final String url = DOWNLOAD_BASE_URL + modelId + URL_SEPARATOR + modelId + EXTENSION;
        return new String(StreamReader.read(new URL(url).openStream()));
    }
