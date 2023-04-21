    private String encryptPassword(String password) throws InvalidPasswordException {
        if (password == null || password.length() == 0) {
            throw new InvalidPasswordException();
        }
        String currentEncryptedPassword = password;
        try {
            MessageDigest md = MessageDigest.getInstance(Constants.DEFAULT_CHECKSUM_ALGORITHM);
            currentEncryptedPassword = new String(currentEncryptedPassword + "NAA-digipres");
            byte[] byteArray = md.digest(currentEncryptedPassword.getBytes());
            String s;
            String currentHexString = "";
            for (byte element : byteArray) {
                s = Integer.toHexString(element & 0xFF);
                if (s.length() == 1) {
                    s = "0" + s;
                }
                currentHexString = currentHexString + s;
            }
            currentEncryptedPassword = currentHexString;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return currentEncryptedPassword;
    }
