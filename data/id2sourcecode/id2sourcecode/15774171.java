    public String load_uri(String uri) {
        try {
            URL url = new URL(uri);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            XQMultiDocument xmd2 = XQMultiDocument.fromXML(is);
            is.close();
            xmd = xmd2;
            command_doc2editor();
            return null;
        } catch (Exception e) {
            return "Failed to load: " + e;
        }
    }
