    public static void main(String[] args) throws IOException {
        URL url = new URL("http://apilab.ncsoft.net:8080/slide/files");
        URLConnection connection = url.openConnection();
        HttpURLConnection hc = (HttpURLConnection) connection;
        hc.setRequestMethod("PROPFIND");
        hc.connect();
        OutputStream outputStream = hc.getOutputStream();
        InputStream inputStream = hc.getInputStream();
    }
