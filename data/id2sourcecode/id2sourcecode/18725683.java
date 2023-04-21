    public static MD5Hash digest(InputStream in) throws IOException {
        final byte[] buffer = new byte[4 * 1024];
        final MessageDigest digester = DIGESTER_FACTORY.get();
        for (int n; (n = in.read(buffer)) != -1; ) {
            digester.update(buffer, 0, n);
        }
        return new MD5Hash(digester.digest());
    }
