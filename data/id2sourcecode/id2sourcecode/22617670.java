    public final byte[] computeOwnerPassword(byte[] ownerPassword, byte[] userPassword, int revision, int length) throws CryptographyException, IOException {
        try {
            byte[] ownerPadded = truncateOrPad(ownerPassword);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ownerPadded);
            byte[] digest = md.digest();
            if (revision == 3 || revision == 4) {
                for (int i = 0; i < 50; i++) {
                    md.reset();
                    md.update(digest, 0, length);
                    digest = md.digest();
                }
            }
            if (revision == 2 && length != 5) {
                throw new CryptographyException("Error: Expected length=5 actual=" + length);
            }
            byte[] rc4Key = new byte[length];
            System.arraycopy(digest, 0, rc4Key, 0, length);
            byte[] paddedUser = truncateOrPad(userPassword);
            rc4.setKey(rc4Key);
            ByteArrayOutputStream crypted = new ByteArrayOutputStream();
            rc4.write(new ByteArrayInputStream(paddedUser), crypted);
            if (revision == 3 || revision == 4) {
                byte[] iterationKey = new byte[rc4Key.length];
                for (int i = 1; i < 20; i++) {
                    System.arraycopy(rc4Key, 0, iterationKey, 0, rc4Key.length);
                    for (int j = 0; j < iterationKey.length; j++) {
                        iterationKey[j] = (byte) (iterationKey[j] ^ (byte) i);
                    }
                    rc4.setKey(iterationKey);
                    ByteArrayInputStream input = new ByteArrayInputStream(crypted.toByteArray());
                    crypted.reset();
                    rc4.write(input, crypted);
                }
            }
            return crypted.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(e.getMessage());
        }
    }
