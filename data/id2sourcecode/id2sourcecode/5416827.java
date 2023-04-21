    public HttpEntity performRequest() throws ClientProtocolException, IOException, WrongLoginException {
        if (this.getRequest() != null) {
            this.setRequestStarted(true);
            this.getHttpClient().getConnectionManager().closeExpiredConnections();
            Logger.getInstance().add("Execute: " + this.getRequest().getURI());
            HttpResponse response = this.getHttpClient().execute(this.getRequest());
            if (response.getFirstHeader("eversync-denied") == null) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return entity;
                }
            } else {
                throw new WrongLoginException("Username or password are wrong");
            }
        } else {
            throw new NullPointerException("No request set");
        }
        return null;
    }
