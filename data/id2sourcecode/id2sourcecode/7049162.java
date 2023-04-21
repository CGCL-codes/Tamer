    @Override
    public boolean validate(String cipherText, String originalText, int pwdCryptType) {
        String newText = "";
        if (pwdCryptType == 1) {
            newText = crypto.encrypt(originalText, Crypto.Encoding.Base64);
        } else {
            newText = messageDigest.digest(originalText);
        }
        return newText.equals(cipherText);
    }
