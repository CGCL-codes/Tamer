    public InputStream getAudio(String text, String languageOutput) throws IOException {
        URL url = new URL(URLCONSTANTS.GOOGLE_TRANSLATE_AUDIO + "q=" + text.replace(" ", "%20") + "&tl=" + languageOutput);
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        InputStream audioSrc = urlConn.getInputStream();
        return new BufferedInputStream(audioSrc);
    }
