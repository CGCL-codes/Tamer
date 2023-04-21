    public static byte[] hash(Object object, availableHashes hashType) throws DPWSException {
        byte[] result = null;
        byte[] serializedObject = Serialization.serializeToBytes(object);
        try {
            MessageDigest md = MessageDigest.getInstance(hashType.toString());
            md.reset();
            result = md.digest(serializedObject);
        } catch (NoSuchAlgorithmException e) {
            throw new DPWSException("NoSuchAlgorithmException");
        }
        return result;
    }
