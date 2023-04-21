    protected URLConnection openConnection(URL url) throws java.io.IOException {
        return new Connection.Client(url);
    }
