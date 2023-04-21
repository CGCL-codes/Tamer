    public void writePassword(CmsDbContext dbc, String userFqn, String oldPassword, String newPassword) throws CmsDataAccessException, CmsPasswordEncryptionException {
        PreparedStatement stmt = null;
        Connection conn = null;
        if (oldPassword != null) {
            readUser(dbc, userFqn, oldPassword, "");
        }
        try {
            conn = getSqlManager().getConnection(dbc);
            stmt = m_sqlManager.getPreparedStatement(conn, "C_USERS_SET_PWD_3");
            stmt.setString(1, OpenCms.getPasswordHandler().digest(newPassword));
            stmt.setString(2, CmsOrganizationalUnit.getSimpleName(userFqn));
            stmt.setString(3, CmsOrganizationalUnit.SEPARATOR + CmsOrganizationalUnit.getParentFqn(userFqn));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new CmsDbSqlException(Messages.get().container(Messages.ERR_GENERIC_SQL_1, CmsDbSqlException.getErrorQuery(stmt)), e);
        } finally {
            m_sqlManager.closeAll(dbc, conn, stmt, null);
        }
    }
