    public boolean getKey5(byte[] password) {
        try {
            byte[] rawkey = pbkdf2(password, salt, iter, keylens[ciphnr]);
            if (ciphnr == 1) {
                for (int i = 0; i < 24; i++) {
                    rawkey[i] = odd_parity[rawkey[i] & 0xff];
                }
            }
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(rawkey);
            md.update(salt);
            byte[] shadig = md.digest();
            for (int i = 0; i < mdigest.length; i++) {
                if (shadig[i] != mdigest[i]) return false;
            }
            if (ciphnr == 0) {
                cipher = new NullCipher();
                key = new SecretKeySpec(rawkey, "NULL");
            } else {
                cipher = Cipher.getInstance(keyalgos[ciphnr]);
                key = new SecretKeySpec(rawkey, keyalgosShort[ciphnr]);
            }
        } catch (GeneralSecurityException ex) {
            throw new InternalError("Can't generate key: " + ex);
        }
        return true;
    }
