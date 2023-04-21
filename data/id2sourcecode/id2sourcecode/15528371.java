    public static final byte[] toByteArray(final InputStream in) throws IOException {
        final ByteArrayOutputStream bo = new ByteArrayOutputStream();
        int length = in.available();
        if (length > 0) {
            int read;
            byte[] buf;
            do {
                buf = new byte[length];
                while ((read = in.read(buf)) > 0) {
                    bo.write(buf, 0, read);
                }
            } while ((length = in.available()) > 0);
        } else {
            int b = 0;
            while ((b = in.read()) > -1) {
                bo.write(b);
            }
        }
        in.close();
        return bo.toByteArray();
    }
