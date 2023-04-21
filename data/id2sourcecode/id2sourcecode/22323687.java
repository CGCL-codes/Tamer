    public BufferedReader makeLeoSearchCall(String toTranslate) throws IOException {
        String toTransEnc = java.net.URLEncoder.encode(toTranslate, "ISO8859_1");
        URL url = new URL("http://dict.leo.org/esde?search=" + toTransEnc);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("ISO8859_1")));
        return in;
    }
