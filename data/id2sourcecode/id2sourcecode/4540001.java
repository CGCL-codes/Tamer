    public boolean checkMd5(byte[] fileContents, String hash) {
        String amsetHashString = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(fileContents);
            BigInteger amsetHash = new BigInteger(1, md5.digest());
            amsetHashString = amsetHash.toString(16);
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return false;
        }
        if (!amsetHashString.equals(hash)) {
            return false;
        } else {
            return true;
        }
    }
