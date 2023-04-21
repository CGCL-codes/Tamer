    public static byte[] encryptSerializable(Serializable s) throws GeneralSecurityException {
        if (s == null) return null;
        byte[] data = null;
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
            oos.close();
            data = baos.toByteArray();
        } catch (final IOException ioe) {
            throw new GeneralSecurityException("Error attempting to serialize object: " + ioe.getMessage());
        }
        final Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        return cipher.doFinal(data);
    }
