    @Override
    protected URLConnection openConnection(URL _url, Proxy _proxy) {
        return new ModuloURLConnection(_url);
    }
