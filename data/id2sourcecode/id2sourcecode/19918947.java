    public static byte[] hashStream(Digest d, InputStream in, long len) throws IOException {
        byte[] buffer = new byte[65536];
        int rc = 0;
        do {
            int nBytes = (len > 65536) ? 65536 : (int) len;
            rc = in.read(buffer, 0, nBytes);
            if (rc > 0) d.update(buffer, 0, rc);
            len -= rc;
        } while ((rc != -1) && (len > 0));
        return d.digest();
    }
