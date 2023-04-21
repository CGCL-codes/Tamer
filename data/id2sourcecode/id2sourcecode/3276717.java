    public static String getShortUrl(String longUrl) {
        String request = "http://api.bit.ly/shorten?version=2.0.1&login=" + BITLY_LOGIN + "&apiKey=" + BITLY_KEY + "&longUrl=" + longUrl;
        String response = "";
        try {
            URL url = new URL(request.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response = response + inputLine;
            }
            in.close();
        } catch (IOException e) {
        }
        int loc = response.indexOf("\"shortUrl\": \"") + 13;
        response = response.substring(loc);
        loc = response.indexOf("\"");
        response = response.substring(0, loc);
        return response;
    }
