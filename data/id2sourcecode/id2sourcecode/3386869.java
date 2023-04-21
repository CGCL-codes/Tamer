    public boolean verify(char[] password) {
        if (password == null) return false;
        md_.reset();
        md_.update(String.valueOf(password).getBytes());
        md_.update(salt_);
        return MessageDigest.isEqual(hash_, md_.digest());
    }
