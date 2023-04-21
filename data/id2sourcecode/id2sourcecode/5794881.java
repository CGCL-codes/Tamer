    public final byte[] computeUserPassword(byte[] password, byte[] o, int permissions, byte[] id, int encRevision, int length) throws CryptographyException, IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] encryptionKey = computeEncryptedKey(password, o, permissions, id, encRevision, length);
        if (encRevision == 2) {
            rc4.setKey(encryptionKey);
            rc4.write(ENCRYPT_PADDING, result);
        } else if (encRevision == 3 || encRevision == 4) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(ENCRYPT_PADDING);
                md.update(id);
                result.write(md.digest());
                byte[] iterationKey = new byte[encryptionKey.length];
                for (int i = 0; i < 20; i++) {
                    System.arraycopy(encryptionKey, 0, iterationKey, 0, iterationKey.length);
                    for (int j = 0; j < iterationKey.length; j++) {
                        iterationKey[j] = (byte) (iterationKey[j] ^ i);
                    }
                    rc4.setKey(iterationKey);
                    ByteArrayInputStream input = new ByteArrayInputStream(result.toByteArray());
                    result.reset();
                    rc4.write(input, result);
                }
                byte[] finalResult = new byte[32];
                System.arraycopy(result.toByteArray(), 0, finalResult, 0, 16);
                System.arraycopy(ENCRYPT_PADDING, 0, finalResult, 16, 16);
                result.reset();
                result.write(finalResult);
            } catch (NoSuchAlgorithmException e) {
                throw new CryptographyException(e);
            }
        }
        return result.toByteArray();
    }
