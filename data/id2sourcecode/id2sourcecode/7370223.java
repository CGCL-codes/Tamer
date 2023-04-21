    private String process(HttpRequest request) throws IOException, AuthorizationDeniedException, ClientProtocolException {
        if (logger.isDebugEnabled()) {
            logger.debug("request line: " + request.getRequestLine());
        }
        HttpResponse response = client.execute(httpHost, request, httpContext);
        int sc = response.getStatusLine().getStatusCode();
        String phrase = response.getStatusLine().getReasonPhrase();
        String body = "";
        if (response.getEntity() != null) {
            InputStream is = response.getEntity().getContent();
            ByteArrayOutputStream res = new ByteArrayOutputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) >= 0) {
                res.write(buf, 0, len);
            }
            is.close();
            body = new String(res.toByteArray());
            if (body.contains("Fedora: 403")) {
                throw new AuthorizationDeniedException("Authorization Denied");
            }
        }
        if (sc < 200 || sc >= 400) {
            throw new ClientProtocolException("Error [Status Code = " + sc + "]" + ": " + phrase);
        }
        return body;
    }
