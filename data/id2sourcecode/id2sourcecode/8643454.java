    public CryptoFileEncryption(String password) {
        byte[] key;
        try {
            key = (SALT + password).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
        } catch (UnsupportedEncodingException e) {
            return;
        } catch (NoSuchAlgorithmException e) {
            return;
        }
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        initCipher(skeySpec);
    }
