    public static void main(String[] args) throws IOException {
        String urltext = "http://www.vogella.de";
        URL url = new URL(urltext);
        String contentType = ((HttpURLConnection) url.openConnection()).getContentType();
        System.out.println(contentType);
    }
