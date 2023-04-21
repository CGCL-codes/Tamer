    public final int getDigestLength() {
        int digestLen = engineGetDigestLength();
        if (digestLen == 0) {
            try {
                MessageDigest md = (MessageDigest) clone();
                byte[] digest = md.digest();
                return digest.length;
            } catch (CloneNotSupportedException e) {
                return digestLen;
            }
        }
        return digestLen;
    }
