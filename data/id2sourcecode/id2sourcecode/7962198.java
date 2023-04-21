    public String encrypt(final Map<String, String> keyChain) {
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(keyChain);
            oos.close();
            final byte[] serialized = bos.toByteArray();
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, m_secretKey);
            final int length = cipher.getOutputSize(serialized.length);
            final byte[] output = new byte[length];
            cipher.doFinal(serialized, 0, serialized.length, output, 0);
            return new String(Base64.encode(output));
        } catch (final Exception e) {
            throw new RuntimeException("Encrypt exception: ", e);
        }
    }
