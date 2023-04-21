    public void open(final String method, final URL url, boolean async, final String userName, final String password) throws IOException {
        this.abort();
        Proxy proxy = this.proxy;
        URLConnection c = proxy == null || proxy == Proxy.NO_PROXY ? url.openConnection() : url.openConnection(proxy);
        synchronized (this) {
            this.connection = c;
            this.async = async;
            this.requestMethod = method;
            this.requestURL = url;
            this.requestUserName = userName;
            this.requestPassword = password;
        }
        this.changeState(AjaxHttpRequest.STATE_LOADING, 0, null, null);
    }
