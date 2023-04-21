    private String submit(URL url, String body) throws IOException {
        OutputStream out = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            if (body != null) {
                conn.setRequestMethod("POST");
                conn.setAllowUserInteraction(false);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestProperty("Content-length", Integer.toString(body.length()));
                out = conn.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print(body);
                pw.flush();
                pw.close();
            } else {
            }
            in = conn.getInputStream();
            BufferedReader rdr = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            CharArrayWriter result = new CharArrayWriter();
            char[] buf = new char[1024];
            int count = rdr.read(buf);
            while (count > 0) {
                result.write(buf, 0, count);
                count = rdr.read(buf);
            }
            return result.toString();
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
