    public String doPost(final URI uri, final List<NameValuePair> params) throws FaceClientException {
        try {
            final HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            postMethod.setURI(uri);
            postMethod.setEntity(entity);
            final HttpResponse response = httpClient.execute(postMethod);
            return EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException cpe) {
            logger.error("Protocol error while POSTing to " + uri, cpe);
            throw new FaceClientException(cpe);
        } catch (IOException ioe) {
            logger.error("Error while POSTing to " + uri, ioe);
            throw new FaceClientException(ioe);
        }
    }
