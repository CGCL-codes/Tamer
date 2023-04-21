    protected String doQuery(String target) throws IOException, ProtocolException {
        DannoClient ac = getClient();
        HttpGet get = new HttpGet(target);
        HttpResponse response = ac.execute(get);
        if (!ac.isOK()) {
            throw new DannoRequestFailureException("GET", response);
        }
        return new BasicResponseHandler().handleResponse(response);
    }
