    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void changePassword(String password) {
        Date now = new Date();
        User loggedInUser = getLoggedInUser();
        if (null != loggedInUser) {
            loggedInUser.updateAuthnPasswordValue(digester.digest(password));
            userDAO.makePersistent(loggedInUser);
            auditLogger.log(now, loggedInUser.getUsername(), ServerSessionUtil.getIP(), "change password", "", true, "");
        } else {
            auditLogger.log(now, "", ServerSessionUtil.getIP(), "change password", "", false, "not logged in");
            throw new RuntimeException("Not logged in");
        }
    }
