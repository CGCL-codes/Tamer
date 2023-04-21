    public URLPictureData(URL url) throws IOException {
        InputStream is = url.openStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] b = new byte[512];
        int read;
        while ((read = is.read(b)) != -1) {
            baos.write(b, 0, read);
        }
        b = null;
        is.close();
        pictureData = baos.toByteArray();
        baos.close();
        this.url = url;
        setCached(false);
    }
