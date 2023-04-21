    public static final byte[] toBytes(InputStream input) throws IOException {
        byte[] aby = new byte[512];
        int r = -1;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        try {
            while ((r = input.read(aby)) != -1) bs.write(aby, 0, r);
            return bs.toByteArray();
        } finally {
            try {
                input.close();
            } catch (Exception ign) {
            }
        }
    }
