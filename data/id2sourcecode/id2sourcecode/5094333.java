    private Hash calculateInfoHash(Map<String, ?> info) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("sha1");
            sha1.update(BEncodedOutputStream.bencode(info));
            return new Hash(sha1.digest(), Hash.Type.SHA1);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
