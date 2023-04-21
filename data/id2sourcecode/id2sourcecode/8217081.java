    public static BufferedReader readFromWeb(String url) throws Exception {
        return new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    }
