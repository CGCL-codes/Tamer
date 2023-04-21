    private String getHashValue() {
        MessageDigest tmpDigest;
        try {
            tmpDigest = (MessageDigest) digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone failed", e);
        }
        byte[] currDigest = tmpDigest.digest();
        BigInteger bigInt = new BigInteger(1, currDigest);
        return String.format("%0" + (currDigest.length << 1) + "x", bigInt);
    }
