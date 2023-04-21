    public boolean isGifFile(String s) {
        URI url;
        try {
            url = new URI(s);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        URLConnection conn = null;
        try {
            conn = url.toURL().openConnection();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStream in = null;
        try {
            in = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            return (in.read() == 'G' && in.read() == 'I' && in.read() == 'F');
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
        return false;
    }
