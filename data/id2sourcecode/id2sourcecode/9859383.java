    private boolean isValidPassword(String password) {
        if (isEncrypted()) {
            return Arrays.equals(digest, Engine.digest(password, getUrl()));
        }
        return true;
    }
