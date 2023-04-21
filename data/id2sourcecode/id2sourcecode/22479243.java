    @SuppressWarnings("unchecked")
    private Password findAllowedPassword(boolean isPasswordId, String password, User user, AccessLevel accessLevel) {
        Password foundPassword = null;
        String sqlAccessLevelIn = null;
        switch(accessLevel) {
            case GRANT:
                sqlAccessLevelIn = "(:aclgrant) ";
                break;
            case WRITE:
                sqlAccessLevelIn = "(:aclwrite, :aclgrant) ";
                break;
            case READ:
                sqlAccessLevelIn = "(:aclread, :aclwrite, :aclgrant) ";
                break;
        }
        StringBuilder hqlString = new StringBuilder();
        hqlString.append("select distinct pw.id from Password pw join pw.permissions pm where ");
        hqlString.append(isPasswordId ? "pw.id = :passwordId " : "pw.name = :passwordName ");
        if (!authorizer.isAuthorized(user, Function.BYPASS_PASSWORD_PERMISSIONS)) {
            hqlString.append(" and pm.accessLevel in ");
            hqlString.append(sqlAccessLevelIn);
            hqlString.append("and ((pm.subject = :user) or (pm.subject in (select g from Group g join g.users u where u = :user)))");
        }
        Query hqlQuery = getSession().createQuery(hqlString.toString());
        if (isPasswordId) {
            hqlQuery.setLong("passwordId", Long.valueOf(password));
        } else {
            hqlQuery.setString("passwordName", password);
        }
        if (!authorizer.isAuthorized(user, Function.BYPASS_PASSWORD_PERMISSIONS)) {
            hqlQuery.setEntity("user", user);
            if (accessLevel.equals(AccessLevel.GRANT) || accessLevel.equals(AccessLevel.WRITE) || accessLevel.equals(AccessLevel.READ)) {
                hqlQuery.setString("aclgrant", AccessLevel.GRANT.name());
            }
            if (accessLevel.equals(AccessLevel.WRITE) || accessLevel.equals(AccessLevel.READ)) {
                hqlQuery.setString("aclwrite", AccessLevel.WRITE.name());
            }
            if (accessLevel.equals(AccessLevel.READ)) {
                hqlQuery.setString("aclread", AccessLevel.READ.name());
            }
        }
        List<Long> passwordIds = hqlQuery.list();
        if (passwordIds.size() > 0) {
            foundPassword = findById(passwordIds.get(0));
            foundPassword.getPermissions().size();
            foundPassword.getTags().size();
        }
        return foundPassword;
    }
