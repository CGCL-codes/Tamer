    private void setProfileProperty(HttpServletRequest request, String name, String value) throws RequestFailureException {
        HttpClient httpclient = buildClient(request);
        try {
            HttpPost post;
            try {
                post = new HttpPost(emmetServiceUrl + "?" + EmmetParameters.ACTION_PARAM + "=" + EmmetAction.setProperty + "&" + EmmetParameters.PROPERTY_NAME_PARAM + "=dannotate-" + name + "&" + EmmetParameters.PROPERTY_VALUE_PARAM + "=" + URLEncoder.encode(value, "UTF-8") + "&format=json");
            } catch (UnsupportedEncodingException ex) {
                throw new ImpossibleException(ex);
            }
            try {
                HttpResponse response = httpclient.execute(post);
                if (response.getStatusLine().getStatusCode() >= 300) {
                    logger.error("Emmet request failed: " + response.getStatusLine());
                    throw new RequestFailureException(500, "Cannot save property '" + name + "'");
                }
            } catch (ClientProtocolException ex) {
                throw new RequestFailureException(500, "Cannot save property '" + name + "'", ex);
            } catch (IOException ex) {
                throw new RequestFailureException(500, "Cannot save property '" + name + "'", ex);
            }
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
