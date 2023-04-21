    public void test_closeWithFixedLengthDisableMod() throws IOException {
        String posted = "just a test";
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", "" + (888));
        OutputStream out = conn.getOutputStream();
        out.write(posted.getBytes());
        out.close();
    }
