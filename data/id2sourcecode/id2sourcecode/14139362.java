    public byte[] doSHA1(byte[] bytes, int lenght) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1Hash = new byte[40];
            md.update(bytes, 0, lenght);
            sha1Hash = md.digest();
            return sha1Hash;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
