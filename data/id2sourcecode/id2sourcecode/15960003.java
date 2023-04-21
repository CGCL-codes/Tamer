    public byte[] engineDoFinal() {
        byte[] hash1;
        byte[] hmac;
        hash1 = md.digest();
        md.reset();
        md.update(opad_key);
        md.update(hash1);
        hmac = md.digest();
        return hmac;
    }
