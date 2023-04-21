    protected String getHashValue(Object obj) throws Exception {
        final String HASH_ALGORITHM = "MD5";
        if (obj == null) return "null";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.close();
            byte[] data = baos.toByteArray();
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] digest = md.digest(data);
            return new String(Base64.encodeBase64(digest), "US-ASCII");
        } catch (Exception e) {
            throw new Exception("Couldn't create " + HASH_ALGORITHM + " hash for given object.", e);
        }
    }
