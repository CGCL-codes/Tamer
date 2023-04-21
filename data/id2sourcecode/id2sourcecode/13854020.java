    private boolean login(String username, String password, StreamWrapper stream) throws WrapperException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        stream.writeOpCode(OPCodes.LOGIN);
        stream.writeString(username);
        if (stream.readBoolean()) {
            byte[] rand = stream.readDataFully();
            byte[] keyBytes = Utils.prepareKey(password);
            Cipher c = Cipher.getInstance("DES/CBC/NoPadding");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1 }));
            byte[] cipherText = new byte[rand.length];
            int ptLength = c.update(rand, 0, rand.length, cipherText, 0);
            ptLength += c.doFinal(cipherText, ptLength);
            stream.writeDataFully(cipherText);
        }
        return stream.readBoolean();
    }
