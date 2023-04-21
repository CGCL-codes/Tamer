    public static byte[] inflate(byte[] data) throws IOException {
        InputStream in = null;
        try {
            in = new InflaterInputStream(new ByteArrayInputStream(data));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[64];
            while (true) {
                int read = in.read(buf, 0, buf.length);
                if (read == -1) break;
                out.write(buf, 0, read);
            }
            return out.toByteArray();
        } catch (OutOfMemoryError oome) {
            throw new IOException(oome.getMessage());
        } finally {
            IOUtils.close(in);
        }
    }
