    public static Checksum calcChecksum(InputStream in, OutputStream out) throws CoreException, IOException {
        long totlen = 0;
        try {
            final int kBUF_SIZE = 1024 * 4;
            MessageDigest digest = getMD5Digest();
            if (digest == null) return null;
            byte buf[] = new byte[kBUF_SIZE];
            int len;
            while ((len = in.read(buf)) > 0) {
                digest.update(buf, 0, len);
                totlen += len;
                if (out != null) out.write(buf, 0, len);
            }
            return new Checksum(toHexString(digest.digest()), totlen);
        } catch (NoSuchAlgorithmException e) {
            throw new CoreException("Exception getting MD5 algorithm");
        }
    }
