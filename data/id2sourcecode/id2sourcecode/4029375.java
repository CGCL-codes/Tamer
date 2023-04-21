    public static void main(String[] args) throws Exception {
        String url = "http://google.com";
        InputStream input = new URL(url).openStream();
        new JerichoHMTLProxifier().doIt(input);
    }
