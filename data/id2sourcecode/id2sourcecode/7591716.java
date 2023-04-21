    public String hash(String data, String hashType) {
        byte[] byteRepresentation = data.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(hashType);
            algorithm.reset();
            algorithm.update(byteRepresentation);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            int i = 0;
            while (i < messageDigest.length) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                i++;
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        }
    }
