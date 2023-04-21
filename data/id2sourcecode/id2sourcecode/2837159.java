    boolean authenticate(String givenUserName, String plaintextPassword) {
        if (this == EMPTY_TOKEN) {
            return false;
        }
        try {
            if (!userName.equals(givenUserName)) {
                return false;
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(plaintextPassword.getBytes());
            if (!(md.isEqual(hash, passwordDigest))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
