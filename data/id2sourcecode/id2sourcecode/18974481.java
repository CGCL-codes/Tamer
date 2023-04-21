    public BufferedReader makeLeoSearchCall(String toTranslate) throws IOException {
        String toTransEnc = java.net.URLEncoder.encode(toTranslate, "ISO8859_1");
        URL url = new URL("http://dict.leo.org/pages.esde/stemming/verb_ar_averiguar.html?Hilfsverb=haber&stamm=bus&stamm2=c&stamm3=qu");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("ISO8859_1")));
        return in;
    }
