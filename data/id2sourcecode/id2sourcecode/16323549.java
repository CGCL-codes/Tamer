    protected final byte[] readResource(String resource) {
        InputStream in = this.getClass().getResourceAsStream(resource);
        if (null != in) {
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                byte[] iob = new byte[0x200];
                int read;
                while (0 < (read = in.read(iob, 0, 0x200))) {
                    buf.write(iob, 0, read);
                }
                return buf.toByteArray();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }
        return null;
    }
