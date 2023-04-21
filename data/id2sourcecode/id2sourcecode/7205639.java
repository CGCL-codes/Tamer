    protected String doPostRDF(String target, String content) throws IOException, ProtocolException {
        DannoClient ac = getClient();
        HttpPost post = new HttpPost(target);
        post.setEntity(new StringEntity(content, "UTF-8"));
        post.addHeader("Content-Type", "application/xml");
        post.addHeader("Accept", "application/xml");
        HttpResponse response = ac.execute(post);
        if (!ac.isOK()) {
            throw new DannoRequestFailureException("POST", response);
        }
        return new BasicResponseHandler().handleResponse(response);
    }
