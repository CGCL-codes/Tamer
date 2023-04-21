    public static String getHttpPostResponseStr(String urlStr, String postStr, String encoding, long timeout) throws Exception, IOException {
        URL url = new URL(urlStr);
        StringBuilder sb = null;
        HttpURLConnection conn = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout((int) timeout);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            if (encoding == null) {
                out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()), true);
            } else {
                out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), encoding), true);
            }
            out.println(postStr);
            out.flush();
            if (encoding == null) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            }
            sb = new StringBuilder(1024);
            readThruBuffer(in, sb);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
            if (conn != null) conn.disconnect();
        }
        return sb.toString();
    }
