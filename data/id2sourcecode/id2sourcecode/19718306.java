    public static byte[] getChecksum(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        try {
            byte[] buffer = new byte[1024];
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            int numRead;
            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    digest.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            return digest.digest();
        } finally {
            is.close();
        }
    }
