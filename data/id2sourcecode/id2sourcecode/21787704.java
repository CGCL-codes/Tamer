    public static String getQuote() {
        String quote = "";
        try {
            URL url = new URL("http://quote-server.appspot.com/rest/tag/money");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            quote = reader.readLine();
            reader.close();
        } catch (Exception e) {
        }
        return quote;
    }
