    public void changePassword(User u, String newPassword) throws UserNotFoundException {
        Random r = new SecureRandom();
        r.nextBytes(u.getSalt());
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
        }
        digest.reset();
        digest.update(u.getSalt());
        u.setPassHash(digest.digest(newPassword.getBytes()));
        updateUser(u);
    }
