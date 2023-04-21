    public String get(String path) throws Exception {
        InputStream in = null;
        StringWriter out = null;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            out = new StringWriter();
            int c = -1;
            while ((c = in.read()) != -1) {
                out.write((char) c);
            }
            return out.toString();
        } finally {
            IOUtilities.close(in);
            IOUtilities.close(out);
        }
    }
