    private void doCrypt() {
        byte[] passwdbytes = unencryptedPasswd.getBytes();
        byte[] saltbytes = salt.getBytes();
        encryptedPasswd = null;
        try {
            MessageDigest algorithm = MessageDigest.getInstance(cryptmethod);
            algorithm.reset();
            algorithm.update(passwdbytes);
            algorithm.update(saltbytes);
            byte[] digest = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String hex = Integer.toHexString(0xFF & digest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
            encryptedPasswd = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
