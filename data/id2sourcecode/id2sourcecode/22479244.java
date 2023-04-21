    @Override
    @SuppressWarnings("unchecked")
    public AccessLevel getMaxEffectiveAccessLevel(Password password, User user) {
        AccessLevel maxEffectiveAccessLevel = null;
        if (authorizer.isAuthorized(user, Function.BYPASS_PASSWORD_PERMISSIONS)) {
            maxEffectiveAccessLevel = AccessLevel.GRANT;
        } else {
            StringBuilder hqlString = new StringBuilder();
            hqlString.append("select distinct pm.accessLevel from Permission pm ");
            hqlString.append("where pm.password = :password ");
            hqlString.append("and pm.accessLevel in (:aclread, :aclwrite, :aclgrant) ");
            hqlString.append("and ((pm.subject = :user) or (pm.subject in (select g from Group g join g.users u where u = :user)))");
            Query hqlQuery = getSession().createQuery(hqlString.toString());
            hqlQuery.setEntity("password", password);
            hqlQuery.setEntity("user", user);
            hqlQuery.setString("aclread", AccessLevel.READ.name());
            hqlQuery.setString("aclwrite", AccessLevel.WRITE.name());
            hqlQuery.setString("aclgrant", AccessLevel.GRANT.name());
            Set<String> accessLevels = new HashSet<String>(hqlQuery.list());
            if (accessLevels.contains(AccessLevel.GRANT.name())) {
                maxEffectiveAccessLevel = AccessLevel.GRANT;
            } else if (accessLevels.contains(AccessLevel.WRITE.name())) {
                maxEffectiveAccessLevel = AccessLevel.WRITE;
            } else if (accessLevels.contains(AccessLevel.READ.name())) {
                maxEffectiveAccessLevel = AccessLevel.READ;
            }
        }
        return maxEffectiveAccessLevel;
    }
