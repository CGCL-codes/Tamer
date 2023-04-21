    RC4 createRC4(int keyBlockNo) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md5.update(_keyDigest);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4);
        new LittleEndianOutputStream(baos).writeInt(keyBlockNo);
        md5.update(baos.toByteArray());
        byte[] digest = md5.digest();
        return new RC4(digest);
    }
