    private static synchronized String[] getDigests(ZipEntry ze, ZipFile zf, MessageDigest[] digests, BASE64Encoder encoder) throws IOException {
        int n, i;
        InputStream is = null;
        try {
            is = zf.getInputStream(ze);
            long left = ze.getSize();
            while ((left > 0) && (n = is.read(buffer, 0, buffer.length)) != -1) {
                for (i = 0; i < digests.length; i++) {
                    digests[i].update(buffer, 0, n);
                }
                left -= n;
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        String[] base64Digests = new String[digests.length];
        for (i = 0; i < digests.length; i++) {
            base64Digests[i] = encoder.encode(digests[i].digest());
        }
        return base64Digests;
    }
