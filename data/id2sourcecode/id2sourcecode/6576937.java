    public boolean isPassword(String password) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        byte[] salt = base64ToByte(super.getSalt());
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());
        System.out.println(getPassHash() + " == " + byteToBase64(hash) + "?");
        return getPassHash().equals(byteToBase64(hash));
    }
