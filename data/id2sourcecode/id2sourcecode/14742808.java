    public static byte[] readURL(URL url) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        InputStream in = url.openStream();
        byte[] block = new byte[5000];
        int read;
        while ((read = in.read(block)) > -1) {
            bout.write(block, 0, read);
        }
        in.close();
        return bout.toByteArray();
    }
