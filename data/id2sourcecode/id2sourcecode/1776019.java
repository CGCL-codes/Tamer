    private OpenSocialHttpResponseMessage execute(OpenSocialHttpMessage request) throws IOException {
        final String method = request.method;
        final boolean isPut = PUT.equalsIgnoreCase(method);
        final boolean isPost = POST.equalsIgnoreCase(method);
        final boolean isDelete = DELETE.equalsIgnoreCase(method);
        final String bodyString = request.getBodyString();
        final String contentType = request.getHeader(HttpMessage.CONTENT_TYPE);
        final OpenSocialUrl url = request.getUrl();
        OpenSocialHttpResponseMessage response = null;
        if (isPut) {
            response = send("PUT", url, contentType, bodyString);
        } else if (isPost) {
            response = send("POST", url, contentType, bodyString);
        } else if (isDelete) {
            response = send("DELETE", url, contentType);
        } else {
            response = send("GET", url, contentType);
        }
        return response;
    }
