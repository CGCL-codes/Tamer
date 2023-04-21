    public final void copyStreamToStream(InputStream is, OutputStream os) throws IOException {
        byte buffer[] = new byte[1024];
        int read;
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
    }
