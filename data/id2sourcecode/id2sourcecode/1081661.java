    public static SimpleCredentialSource newInstance(URL url) throws IOException {
        return newInstance(url.openStream());
    }
