    public void test_writeWithFixedLengthDisableMode() throws IOException {
        String bigString = "big String:/modules/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnectionImpl.java b/modules/luni/src/main/java/org/apache/harmony/luni/internal/net/www/protocol/http/HttpURLConnectionImpl.java";
        java.net.HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Length", "" + (168));
        OutputStream out = httpURLConnection.getOutputStream();
        out.write(bigString.getBytes());
    }
