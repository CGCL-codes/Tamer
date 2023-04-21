    @Override
    public OpenSocialHttpResponseMessage execute(HttpMessage request, Map<String, Object> parameters) throws IOException {
        String body = null;
        if (request.getBody() != null) {
            body = new String(toByteArray(request.getBody()));
        }
        OpenSocialHttpMessage openSocialRequest = new OpenSocialHttpMessage(request.method, new OpenSocialUrl(request.url.toExternalForm()), body);
        return execute(openSocialRequest);
    }
