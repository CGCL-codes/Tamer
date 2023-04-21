    public static String readURL(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = null;
        try {
            conn.setRequestMethod("GET");
            is = conn.getInputStream();
            String contentEncoding = conn.getContentEncoding();
            if (contentEncoding == null) {
                contentEncoding = "UTF-8";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is, contentEncoding));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(line);
            }
            return sb.toString();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (is != null) {
                is.close();
            }
        }
    }
