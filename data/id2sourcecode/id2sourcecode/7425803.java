    public boolean isJpegFile(String s) throws IOException {
        URI url;
        try {
            url = new URI(s);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
        URLConnection conn = url.toURL().openConnection();
        InputStream in = new BufferedInputStream(conn.getInputStream());
        try {
            return (in.read() == 'J' && in.read() == 'F' && in.read() == 'I' && in.read() == 'F');
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
    }
