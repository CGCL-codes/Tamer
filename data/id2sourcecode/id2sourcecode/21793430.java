    public static final byte[] downloadHttpFile(String url) {
        HttpURLConnection uconn = null;
        short httpStatusCode;
        byte[] contents = null;
        try {
            uconn = (HttpURLConnection) new java.net.URL(url).openConnection();
            httpStatusCode = (short) uconn.getResponseCode();
        } catch (Exception e) {
            return null;
        }
        if (httpStatusCode == 200) {
            try {
                BufferedInputStream bis = new BufferedInputStream(uconn.getInputStream(), 1024);
                int length = uconn.getContentLength();
                contents = new byte[length];
                bis.read(contents);
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
                contents = null;
            } finally {
                uconn.disconnect();
            }
        } else if (httpStatusCode >= 300 && httpStatusCode <= 399) {
            if (uconn.getHeaderField("Location") != null) {
                url = uconn.getHeaderField("Location").trim();
                uconn.disconnect();
                contents = downloadHttpFile(url);
            }
        }
        return contents;
    }
