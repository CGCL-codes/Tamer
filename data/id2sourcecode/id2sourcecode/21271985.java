    public static void main(String[] args) throws Exception {
        org.apache.log4j.BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        HTTPRequest http = new HTTPRequest("http://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/");
        String s = http.getString();
        LOG.info(s);
        String language = "en";
        String toSpeak = "hello";
        URI uri = new URI("http", null, "translate.google.com", 80, "/translate_tts", "tl=" + language + "&q=" + toSpeak, null);
        URL url = uri.toURL();
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        System.out.println("Response code = " + connection.getResponseCode());
        String header = connection.getHeaderField("location");
        if (header != null) System.out.println("Redirected to " + header);
        HTTPRequest request = new HTTPRequest(uri.toURL());
        request.getBinary();
    }
