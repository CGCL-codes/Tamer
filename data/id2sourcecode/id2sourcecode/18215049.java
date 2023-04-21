    public void writePassword(CmsDbContext dbc, String userFqn, String oldPassword, String newPassword) throws CmsDataAccessException, CmsPasswordEncryptionException {
        if (oldPassword != null) {
            readUser(dbc, userFqn, oldPassword, "");
        }
        try {
            Query q = m_sqlManager.createQuery(dbc, C_USERS_SET_PWD_3);
            q.setParameter(1, CmsOrganizationalUnit.getSimpleName(userFqn));
            q.setParameter(2, CmsOrganizationalUnit.SEPARATOR + CmsOrganizationalUnit.getParentFqn(userFqn));
            List<CmsDAOUsers> res = q.getResultList();
            for (CmsDAOUsers u : res) {
                u.setUserPassword(OpenCms.getPasswordHandler().digest(newPassword));
            }
        } catch (PersistenceException e) {
            throw new CmsDataAccessException(Messages.get().container(Messages.ERR_JPA_PERSITENCE, e), e);
        }
    }
