    @Override
    protected URLConnection openConnection(URL _url) {
        return new ModuloURLConnection(_url);
    }
