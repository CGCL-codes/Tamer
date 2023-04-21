    private byte[] createNotAvailableImage(String contentType) throws IOException {
        StringBuffer name = new StringBuffer("com/mysticcoders/resources/ImageNotAvailable.");
        if ("image/jpeg".equalsIgnoreCase(contentType)) {
            name.append("jpg");
        } else if ("image/png".equalsIgnoreCase(contentType)) {
            name.append("png");
        } else {
            name.append("gif");
        }
        URL url = getClass().getClassLoader().getResource(name.toString());
        InputStream in = url.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        in.close();
        out.close();
        return out.toByteArray();
    }
