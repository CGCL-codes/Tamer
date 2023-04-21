    public String doPost(final File file, final URI uri, final List<NameValuePair> params) throws FaceClientException {
        try {
            final MultipartEntity entity = new MultipartEntity();
            entity.addPart("image", new FileBody(file));
            try {
                for (NameValuePair nvp : params) {
                    entity.addPart(nvp.getName(), new StringBody(nvp.getValue()));
                }
            } catch (UnsupportedEncodingException uee) {
                logger.error("Error adding entity", uee);
                throw new FaceClientException(uee);
            }
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
