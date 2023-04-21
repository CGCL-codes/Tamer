    private void setDudleySharingView(HttpServletRequest request, String uri) {
        log.info("Begin setDudleySharingView - read/write for Dudley Lab");
        final String addamaUrl = "/addama-sharing/viewing" + uri;
        PostMethod post = new PostMethod(getBaseUrl(request) + addamaUrl);
        try {
            JSONObject annotationJson = new JSONObject();
            annotationJson.put("everybody", "true");
            post.setQueryString(new NameValuePair[] { new NameValuePair("acl", annotationJson.toString()) });
            this.httpClientTemplate.executeMethod(post, new ResponseCallback() {

                public Object onResponse(int status, HttpMethod httpMethod) throws HttpClientResponseException {
                    if (status != 200) {
                        log.error("setDudleySharingView post request failed with status:" + status);
                        throw new HttpClientResponseException(status, httpMethod, null);
                    } else {
                        log.info("Posted OKAY request on:" + addamaUrl);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error exception:" + e + "\nURL:" + addamaUrl);
        } finally {
            post.releaseConnection();
            log.info("released connection for url " + addamaUrl);
        }
        log.info("End setDudleySharingView (" + addamaUrl + ")," + " uri: " + uri);
    }
