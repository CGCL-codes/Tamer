    public static String ping(String url) throws IOException {
        HttpURLConnection hc = null;
        InputStream input = null;
        try {
            hc = (HttpURLConnection) new URL(url).openConnection();
            hc.setRequestMethod("GET");
            hc.setUseCaches(false);
            hc.connect();
            int responseCode = hc.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                input = hc.getInputStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                int read = 0;
                byte[] buffer = new byte[1024];
                for (; ; ) {
                    int n = input.read(buffer);
                    if (n == (-1)) break;
                    output.write(buffer, 0, n);
                    read += n;
                    if (read > MAX_SIZE) throw new IOException("Size is out of max size: " + MAX_SIZE);
                }
                output.close();
                byte[] data = output.toByteArray();
                return new String(data);
            } else {
                throw new IOException("Not 200 OK returned.");
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (hc != null) {
                hc.disconnect();
            }
        }
    }
