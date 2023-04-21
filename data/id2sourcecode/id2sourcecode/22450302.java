    public boolean authenticate(String userName, String loginPassword) {
        AkteraUser user = userDAO.findUserByName(userName);
        if (user == null || !StringTools.digest(loginPassword).equals(user.getPassword())) {
            return false;
        }
        return true;
    }
