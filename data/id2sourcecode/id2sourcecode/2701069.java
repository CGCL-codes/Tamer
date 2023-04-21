        InputStream send(URL url, File file, String pass, boolean chunk) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            OutputStream out = null;
            InputStream in = null;
            if (file != null) {
                conn.addRequestProperty("File", file.getName());
                conn.addRequestProperty("Size", "" + file.length());
                if (pass != null) {
                    conn.addRequestProperty("Pass", pass);
                }
                if (chunk) {
                    conn.setChunkedStreamingMode(0);
                }
                conn.setDoOutput(true);
                out = conn.getOutputStream();
                in = new FileInputStream(file);
                pipe(in, out);
                out.flush();
                in.close();
            }
            int code = conn.getResponseCode();
            if (code == 200) {
                in = conn.getInputStream();
            } else if (code < 0) {
                throw new IOException("HTTP response unreadable.");
            } else {
                in = conn.getErrorStream();
            }
            return in;
        }
