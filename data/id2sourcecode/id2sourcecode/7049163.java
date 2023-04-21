    @Override
    public String digest(String originalText, int pwdCryptType) {
        String pinBlock = "";
        if (pwdCryptType == 1) {
            pinBlock = crypto.encrypt(originalText, Crypto.Encoding.Base64);
        } else {
            pinBlock = messageDigest.digest(originalText);
        }
        return pinBlock;
    }
