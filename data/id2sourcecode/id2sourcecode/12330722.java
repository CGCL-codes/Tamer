    public ClientHttpRequest(URL url) throws IOException {
        this(url.openConnection(Global.getProxy()));
    }
