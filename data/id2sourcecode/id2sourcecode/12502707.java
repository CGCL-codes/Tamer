    protected URLConnection openConnection(URL url) throws IOException {
        String protocol = url.getProtocol();
        if (protocol.equals("help")) {
            return new HelpURLConnection(url);
        }
        return null;
    }
