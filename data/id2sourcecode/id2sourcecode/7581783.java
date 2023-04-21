    private static String post(String xmlString) throws IOException {
        final URL url = new URL(CELLACT_GATEWAY_URL);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        final String encodedMessage = "XMLString=" + xmlString.replace(" ", "%20").replace("+", "%2B");
        final byte[] byteMessage = encodedMessage.getBytes("UTF-8");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Length", Integer.toString(byteMessage.length));
        conn.connect();
        final OutputStream os = conn.getOutputStream();
        try {
            os.write(byteMessage);
        } finally {
            os.close();
        }
        final StringBuilder builder = new StringBuilder();
        final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        try {
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            br.close();
        }
        return builder.toString();
    }
