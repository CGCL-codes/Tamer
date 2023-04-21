    public boolean verifyHash(byte[] data, byte[] hashValue, String algorithm) throws NoSuchAlgorithmException {
        byte[] hashValueComp;
        MessageDigest hash = MessageDigest.getInstance(algorithm);
        hash.update(data);
        hashValueComp = hash.digest();
        int len1, len2, i = 0;
        len1 = hashValue.length;
        len2 = hashValueComp.length;
        if (len1 == len2) {
            for (i = 0; i < len1; i++) {
                if (hashValue[i] != hashValueComp[i]) break;
            }
        }
        if (i == len1) return true; else return false;
    }
