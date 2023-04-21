    private byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int read = -1;
        while ((read = in.read(buffer)) > -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        return out.toByteArray();
    }
