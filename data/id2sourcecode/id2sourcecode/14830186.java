    public static void main(String[] args) throws IOException {
        String urltext = "http://www.vogella.de";
        URL url = new URL(urltext);
        int responseCode = ((HttpURLConnection) url.openConnection()).getResponseCode();
        System.out.println(responseCode);
    }
