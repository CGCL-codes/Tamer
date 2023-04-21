    static byte[] getBinaryPassword(int[] salt, boolean usingNewPasswords) throws NoSuchAlgorithmException {
        int val = 0;
        byte[] binaryPassword = new byte[SHA1_HASH_SIZE];
        if (usingNewPasswords) {
            int pos = 0;
            for (int i = 0; i < 4; i++) {
                val = salt[i];
                for (int t = 3; t >= 0; t--) {
                    binaryPassword[pos++] = (byte) (val & 255);
                    val >>= 8;
                }
            }
            return binaryPassword;
        }
        int offset = 0;
        for (int i = 0; i < 2; i++) {
            val = salt[i];
            for (int t = 3; t >= 0; t--) {
                binaryPassword[t + offset] = (byte) (val % 256);
                val >>= 8;
            }
            offset += 4;
        }
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(binaryPassword, 0, 8);
        return md.digest();
    }
