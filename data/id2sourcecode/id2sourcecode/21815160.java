    private byte[] deriveCipherKey(Key key) {
        byte[] result = null;
        byte[] passwdBytes = key.getEncoded();
        if (algo.equals("DES")) {
            byte[] concat = new byte[passwdBytes.length + salt.length];
            System.arraycopy(passwdBytes, 0, concat, 0, passwdBytes.length);
            java.util.Arrays.fill(passwdBytes, (byte) 0x00);
            System.arraycopy(salt, 0, concat, passwdBytes.length, salt.length);
            byte[] toBeHashed = concat;
            for (int i = 0; i < iCount; i++) {
                md.update(toBeHashed);
                toBeHashed = md.digest();
            }
            java.util.Arrays.fill(concat, (byte) 0x00);
            result = toBeHashed;
        } else if (algo.equals("DESede")) {
            int i;
            for (i = 0; i < 4; i++) {
                if (salt[i] != salt[i + 4]) break;
            }
            if (i == 4) {
                for (i = 0; i < 2; i++) {
                    byte tmp = salt[i];
                    salt[i] = salt[3 - i];
                    salt[3 - 1] = tmp;
                }
            }
            byte[] kBytes = null;
            IvParameterSpec iv = null;
            byte[] toBeHashed = null;
            result = new byte[DESedeKeySpec.DES_EDE_KEY_LEN + DESConstants.DES_BLOCK_SIZE];
            for (i = 0; i < 2; i++) {
                toBeHashed = new byte[salt.length / 2];
                System.arraycopy(salt, i * (salt.length / 2), toBeHashed, 0, toBeHashed.length);
                for (int j = 0; j < iCount; j++) {
                    md.update(toBeHashed);
                    md.update(passwdBytes);
                    toBeHashed = md.digest();
                }
                System.arraycopy(toBeHashed, 0, result, i * 16, toBeHashed.length);
            }
        }
        return result;
    }
