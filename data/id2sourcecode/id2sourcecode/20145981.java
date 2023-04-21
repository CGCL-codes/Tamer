    public final byte[] getMessage(final int index) throws IOException {
        InputStream in = getMessageAsStream(index);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int read;
        while ((read = in.read()) != -1) {
            bout.write(read);
        }
        return bout.toByteArray();
    }
