    public static byte[] computeChecksum(File filename, String alg) throws IOException, NoSuchAlgorithmException {
        final InputStream fis = new FileInputStream(filename);
        try {
            final byte[] buffer = new byte[1024];
            final MessageDigest complete = MessageDigest.getInstance(alg);
            int numRead = fis.read(buffer);
            while (numRead != -1) {
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
                numRead = fis.read(buffer);
            }
            return complete.digest();
        } finally {
            fis.close();
        }
    }
