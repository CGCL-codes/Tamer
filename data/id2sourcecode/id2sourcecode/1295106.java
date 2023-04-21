    public static Object post(URL url, byte[] bytes, String userAgent, String contentType, Parser parser) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        if (userAgent != null) {
            httpURLConnection.setRequestProperty("User-Agent", userAgent);
        }
        if (contentType != null) {
            httpURLConnection.setRequestProperty("Content-Type", contentType);
        }
        httpURLConnection.setRequestProperty("Content-Length", Integer.toString(bytes.length));
        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(bytes);
        bufferedOutputStream.close();
        if (parser != null) {
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int contentLength = httpURLConnection.getContentLength();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Object object = parser.parse(bufferedInputStream, contentLength);
                bufferedInputStream.close();
                return object;
            }
        }
        return null;
    }
