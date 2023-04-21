    public static Node updateUser(Node user, String prefix, String firstName, String otherNames, String lastName, String suffix, String email, String password, String[] supervisors) throws RepositoryException {
        user.setProperty("prefix", prefix);
        user.setProperty("firstName", firstName);
        user.setProperty("otherNames", otherNames);
        user.setProperty("lastName", lastName);
        user.setProperty("suffix", suffix);
        user.setProperty("email", email);
        user.setProperty("supervisors", supervisors);
        if (password != null && !password.equals("")) {
            user.setProperty("password", DigestUtils.digest(password));
        }
        return user;
    }
