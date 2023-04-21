    public CmsUser readUser(CmsDbContext dbc, String userFqn, String password, String remoteAddress) throws CmsDataAccessException, CmsPasswordEncryptionException {
        PreparedStatement stmt = null;
        ResultSet res = null;
        CmsUser user = null;
        Connection conn = null;
        try {
            conn = m_sqlManager.getConnection(dbc);
            stmt = m_sqlManager.getPreparedStatement(conn, "C_USERS_READ_WITH_PWD_3");
            stmt.setString(1, CmsOrganizationalUnit.getSimpleName(userFqn));
            stmt.setString(2, CmsOrganizationalUnit.SEPARATOR + CmsOrganizationalUnit.getParentFqn(userFqn));
            stmt.setString(3, OpenCms.getPasswordHandler().digest(password));
            res = stmt.executeQuery();
            if (res.next()) {
                user = internalCreateUser(dbc, res);
                while (res.next()) {
                }
            } else {
                CmsMessageContainer message = org.opencms.db.Messages.get().container(org.opencms.db.Messages.ERR_UNKNOWN_USER_1, userFqn);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(message.key());
                }
                throw new CmsDbEntryNotFoundException(message);
            }
            return user;
        } catch (SQLException e) {
            throw new CmsDbSqlException(Messages.get().container(Messages.ERR_GENERIC_SQL_1, CmsDbSqlException.getErrorQuery(stmt)), e);
        } finally {
            m_sqlManager.closeAll(dbc, conn, stmt, res);
        }
    }
