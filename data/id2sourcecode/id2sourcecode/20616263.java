    public static byte[] hash(Object object, availableHashes hashType) throws SecurityException {
        byte[] result = null;
        byte[] serializedObject = SerializationCore.serializeToBytes(object);
        try {
            MessageDigest md = MessageDigest.getInstance(hashType.toString(), "BC");
            md.reset();
            result = md.digest(serializedObject);
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (NoSuchProviderException e) {
            throw new SecurityException(e);
        }
        return result;
    }
