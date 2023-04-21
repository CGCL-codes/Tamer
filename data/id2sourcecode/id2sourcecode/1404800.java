    public static byte[] getMD5(InputStream is, OutputStream os) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(new BufferedInputStream(is), digest);
            BufferedOutputStream bos = null;
            if (os != null) {
                bos = new BufferedOutputStream(os);
            }
            byte[] buf = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            while ((bytesRead = dis.read(buf, 0, BUFFER_SIZE)) != -1) {
                if (os != null) {
                    bos.write(buf, 0, bytesRead);
                }
            }
            if (os != null) {
                bos.flush();
            }
            dis.close();
            is.close();
            return digest.digest();
        } catch (NoSuchAlgorithmException nsae) {
            throw new UnexpectedException(nsae, "MD5 algorithm not available");
        }
    }
