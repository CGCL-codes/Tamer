    private static String hashString(String toHash) {
        final StringBuilder hashed = new StringBuilder();
        MessageDigest algorithm = null;
        byte[] toHashBytes = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            toHashBytes = toHash.getBytes("UTF-8");
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(PasswordHashing.class.getPackage().getName()).log(Level.WARNING, "ERROR: UnsupportedEncodingException thrown! String Not Hashed!", e);
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger(PasswordHashing.class.getPackage().getName()).log(Level.WARNING, "ERROR: UnsupportedEncodingException thrown! String Not Hashed!", e);
        }
        algorithm.reset();
        algorithm.update(toHashBytes);
        byte[] digested = algorithm.digest();
        for (byte element : digested) {
            hashed.append(Character.forDigit((element >> 4) & 0xf, 16));
            hashed.append(Character.forDigit(element & 0xf, 16));
        }
        return hashed.toString();
    }
