    private static Result sendImpl(Message inforuMessage) throws IOException {
        final URL url = new URL(INFORU_GATEWAY_URL);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        final String encodedMessage = "InforuXML=" + inforuMessage.toXml().replace(' ', '+');
        final byte[] byteMessage = encodedMessage.getBytes("UTF-8");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", Integer.toString(byteMessage.length));
        conn.connect();
        final OutputStream os = conn.getOutputStream();
        try {
            os.write(byteMessage);
        } finally {
            os.close();
        }
        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        try {
            return Result.fromXml(br.readLine());
        } finally {
            br.close();
        }
    }
