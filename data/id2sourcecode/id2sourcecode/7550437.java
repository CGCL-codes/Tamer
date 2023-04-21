    public User validate(String login, String password) throws UserNotFoundException {
        User user = getUser(login);
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
        }
        digest.reset();
        digest.update(user.getSalt());
        byte[] pwHash = digest.digest(password.getBytes());
        if (!Arrays.equals(pwHash, user.getPassHash())) {
            throw new UserNotFoundException();
        }
        return user;
    }
