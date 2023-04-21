    public CmsUser readUser(CmsDbContext dbc, String userFqn, String password, String remoteAddress) throws CmsDataAccessException, CmsPasswordEncryptionException {
        CmsUser user = null;
        try {
            Query q = m_sqlManager.createQuery(dbc, C_USERS_READ_WITH_PWD_3);
            q.setParameter(1, CmsOrganizationalUnit.getSimpleName(userFqn));
            q.setParameter(2, CmsOrganizationalUnit.SEPARATOR + CmsOrganizationalUnit.getParentFqn(userFqn));
            q.setParameter(3, OpenCms.getPasswordHandler().digest(password));
            try {
                CmsDAOUsers u = (CmsDAOUsers) q.getSingleResult();
                user = internalCreateUser(dbc, u);
            } catch (NoResultException e) {
                CmsMessageContainer message = org.opencms.db.Messages.get().container(org.opencms.db.Messages.ERR_UNKNOWN_USER_1, userFqn);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(message.key());
                }
                throw new CmsDbEntryNotFoundException(message);
            }
            return user;
        } catch (PersistenceException e) {
            throw new CmsDataAccessException(Messages.get().container(Messages.ERR_JPA_PERSITENCE, e), e);
        }
    }
