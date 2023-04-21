    public final byte[] getUserPassword(byte[] ownerPassword, byte[] o, int revision, long length) throws CryptographyException, IOException {
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] ownerPadded = truncateOrPad(ownerPassword);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ownerPadded);
            byte[] digest = md.digest();
            if (revision == 3 || revision == 4) {
                for (int i = 0; i < 50; i++) {
                    md.reset();
                    md.update(digest);
                    digest = md.digest();
                }
            }
            if (revision == 2 && length != 5) {
                throw new CryptographyException("Error: Expected length=5 actual=" + length);
            }
            byte[] rc4Key = new byte[(int) length];
            System.arraycopy(digest, 0, rc4Key, 0, (int) length);
            if (revision == 2) {
                rc4.setKey(rc4Key);
                rc4.write(o, result);
            } else if (revision == 3 || revision == 4) {
                byte[] iterationKey = new byte[rc4Key.length];
                byte[] otemp = new byte[o.length];
                System.arraycopy(o, 0, otemp, 0, o.length);
                rc4.write(o, result);
                for (int i = 19; i >= 0; i--) {
                    System.arraycopy(rc4Key, 0, iterationKey, 0, rc4Key.length);
                    for (int j = 0; j < iterationKey.length; j++) {
                        iterationKey[j] = (byte) (iterationKey[j] ^ (byte) i);
                    }
                    rc4.setKey(iterationKey);
                    result.reset();
                    rc4.write(otemp, result);
                    otemp = result.toByteArray();
                }
            }
            return result.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(e);
        }
    }
