    public byte[] gen(byte[] key, byte[] text) {
        if (key.length > mdBlockSize_) {
            messageDigest_.update(key);
            key = messageDigest_.digest();
        }
        byte[] extKey = new byte[mdBlockSize_];
        System.arraycopy(key, 0, extKey, 0, key.length);
        byte[] keyXorIpad = xor(key, ipad_);
        messageDigest_.update(keyXorIpad);
        messageDigest_.update(text);
        byte[] tmp = messageDigest_.digest();
        byte[] keyXorOpad = xor(key, opad_);
        messageDigest_.update(keyXorOpad);
        messageDigest_.update(tmp);
        byte[] out = messageDigest_.digest();
        return out;
    }
