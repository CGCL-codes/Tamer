    protected String doPutRDF(String target, String content) throws IOException, ProtocolException {
        DannoClient ac = getClient();
        HttpPut put = new HttpPut(target);
        put.setEntity(new StringEntity(content, "UTF-8"));
        put.addHeader("Content-Type", "application/xml");
        put.addHeader("Accept", "application/xml");
        HttpResponse response = ac.execute(put);
        if (!ac.isOK()) {
            throw new DannoRequestFailureException("PUT", response);
        }
        return new BasicResponseHandler().handleResponse(response);
    }
