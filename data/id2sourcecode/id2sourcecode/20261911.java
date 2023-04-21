    public static byte[] getMD5Hash(InputStream ins, long offset, long length) throws IOException {
        if (ins == null) {
            throw new IllegalArgumentException("ins should not be null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset should not be negative");
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ins.skip(offset);
        int bytesRead = 0;
        if (length > 0) {
            byte[] bytes = new byte[(int) length];
            bytesRead = ins.read(bytes);
            if (bytesRead < length) {
                byte[] lastBytes = new byte[bytesRead];
                System.arraycopy(bytes, 0, lastBytes, 0, lastBytes.length);
                md.update(lastBytes);
                return md.digest();
            }
            md.update(bytes);
        } else {
            byte[] bytes = new byte[MD5_BUFFER_SIZE];
            while (bytesRead > -1) {
                bytesRead = ins.read(bytes);
                if (bytesRead < MD5_BUFFER_SIZE) {
                    byte[] lastBytes = new byte[bytesRead];
                    System.arraycopy(bytes, 0, lastBytes, 0, lastBytes.length);
                    md.update(lastBytes);
                    return md.digest();
                } else {
                    md.update(bytes);
                }
            }
        }
        return md.digest();
    }
