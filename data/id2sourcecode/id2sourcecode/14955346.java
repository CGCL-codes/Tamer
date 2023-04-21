    public String doGet(final URI uri) throws FaceClientException {
        getMethod.setURI(uri);
        try {
            HttpResponse response = httpClient.execute(getMethod);
            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException cpe) {
            logger.error("Protocol error while POSTing to " + uri, cpe);
            throw new FaceClientException(cpe);
        } catch (IOException ioe) {
            logger.error("Error while POSTing to " + uri, ioe);
            throw new FaceClientException(ioe);
        }
    }
