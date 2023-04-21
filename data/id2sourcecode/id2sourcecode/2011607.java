    private HttpURLConnection createConnection(boolean multipart) throws IOException {
        StringBuilder buf = new StringBuilder();
        buf.append("http://");
        buf.append(hostName);
        buf.append(':');
        buf.append(EYEFI_PORT);
        buf.append("/api/soap/eyefilm/v1");
        if (multipart) {
            buf.append("/upload");
        }
        URL url = new URL(buf.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-agent", "Eye-Fi Card/4.5022");
        con.setRequestProperty("Host", "api.eye.fi");
        return con;
    }
