    public static byte[] getDigest(InputSupplier<? extends InputStream> supplier, final MessageDigest md) throws IOException {
        return readBytes(supplier, new ByteProcessor<byte[]>() {

            public boolean processBytes(byte[] buf, int off, int len) {
                md.update(buf, off, len);
                return true;
            }

            public byte[] getResult() {
                return md.digest();
            }
        });
    }
