    public static byte[] encodePapPassword(final MessageDigest md5, final byte[] userPass, final byte[] requestAuthenticator, final String sharedSecret) {
        byte[] userPassBytes = null;
        if (userPass.length > 128) {
            userPassBytes = new byte[128];
            System.arraycopy(userPass, 0, userPassBytes, 0, 128);
        } else {
            userPassBytes = userPass;
        }
        byte[] encryptedPass = null;
        if (userPassBytes.length < 128) {
            if (userPassBytes.length % 16 == 0) {
                encryptedPass = new byte[userPassBytes.length];
            } else {
                encryptedPass = new byte[((userPassBytes.length / 16) * 16) + 16];
            }
        } else {
            encryptedPass = new byte[128];
        }
        System.arraycopy(userPassBytes, 0, encryptedPass, 0, userPassBytes.length);
        for (int i = userPassBytes.length; i < encryptedPass.length; i++) {
            encryptedPass[i] = 0;
        }
        md5.reset();
        md5.update(sharedSecret.getBytes());
        md5.update(requestAuthenticator);
        byte bn[] = md5.digest();
        for (int i = 0; i < 16; i++) {
            encryptedPass[i] = (byte) (bn[i] ^ encryptedPass[i]);
        }
        if (encryptedPass.length > 16) {
            for (int i = 16; i < encryptedPass.length; i += 16) {
                md5.reset();
                md5.update(sharedSecret.getBytes());
                md5.update(encryptedPass, i - 16, 16);
                bn = md5.digest();
                for (int j = 0; j < 16; j++) {
                    encryptedPass[i + j] = (byte) (bn[j] ^ encryptedPass[i + j]);
                }
            }
        }
        return encryptedPass;
    }
