    public byte[] sign(byte[] message) {
        Debug.println("gale.sign", "GalePrivateKey.sign() message");
        Debug.println("gale.sign", message);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Debug.assrt(false);
        }
        byte[] hash = digest.digest(message);
        byte[] digestInfo = new byte[DIGEST_INFO_LEN];
        System.arraycopy(DIGEST_INFO_A, 0, digestInfo, 0, DIGEST_INFO_A.length);
        digestInfo[DIGEST_INFO_A.length] = (byte) 5;
        System.arraycopy(DIGEST_INFO_B, 0, digestInfo, DIGEST_INFO_A.length + 1, DIGEST_INFO_B.length);
        System.arraycopy(hash, 0, digestInfo, DIGEST_INFO_A.length + 1 + DIGEST_INFO_B.length, hash.length);
        Debug.println("gale.sign", "sign() digestInfo");
        Debug.println("gale.sign", digestInfo);
        BigInteger signatureInt = privateEncrypt(digestInfo);
        Debug.assrt(signatureInt.signum() == 1);
        byte[] toReturn = signatureInt.toByteArray();
        if (toReturn[0] == 0) {
            byte[] newSigData = new byte[toReturn.length - 1];
            System.arraycopy(toReturn, 1, newSigData, 0, newSigData.length);
            toReturn = newSigData;
        }
        Debug.println("gale.sign", "sign() SIGNATURE");
        Debug.println("gale.sign", toReturn);
        return toReturn;
    }
