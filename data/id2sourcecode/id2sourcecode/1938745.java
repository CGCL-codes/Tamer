    private static String computeDigest(InputStream in, String algorithm) throws IOException, NoSuchAlgorithmException {
        try {
            if (!(in instanceof BufferedInputStream)) {
                in = new BufferedInputStream(in, 2048);
            }
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] buf = new byte[2048];
            while (true) {
                int n = in.read(buf);
                if (n < 0) {
                    break;
                }
                md.update(buf, 0, n);
            }
            return digestString(md.digest());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }
