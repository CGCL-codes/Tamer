    private String readURL(URL url) throws IOException {
        InputStreamReader sr = null;
        try {
            StringBuilder sb = new StringBuilder(100);
            InputStream is = url.openStream();
            sr = new InputStreamReader(is, "UTF-8");
            int i = -1;
            while ((i = sr.read()) != -1) {
                if (i != 65279) {
                    sb.append((char) i);
                }
            }
            return sb.toString();
        } finally {
            IOUtil.close(sr);
        }
    }
