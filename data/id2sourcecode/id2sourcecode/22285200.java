    private byte[] computeChecksum(byte[] array) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(Globals.getCHECKSUM_MODE());
            md.update(array);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
