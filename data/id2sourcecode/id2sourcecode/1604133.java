    public String generateNewPasswordHash(String plaintext) {
        StringBuffer pwhash = new StringBuffer("sha1$");
        Random r = new Random();
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] salt = new byte[5];
        r.nextBytes(salt);
        pwhash.append(toHex(salt) + "$");
        md.update(salt);
        pwhash.append(toHex(md.digest(plaintext.getBytes())));
        return pwhash.toString();
    }
