    public void addSignFields(String id, HashMap<String, String> fields) {
        logger.debug("addSignFields(%s)", id);
        Connection connection = null;
        NamedParameterStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);
            for (Entry<String, String> e : fields.entrySet()) {
                statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_SIGN_FIELDS));
                statement.setString(QueryStore.INSERT_SIGN_FIELDS_PARAM_ID_TEMPLATE, id);
                statement.setString(QueryStore.INSERT_SIGN_FIELDS_PARAM_SIGN_NAME, e.getKey());
                statement.setString(QueryStore.INSERT_SIGN_FIELDS_PARAM_ROLES, e.getValue());
                statement.executeUpdate();
                statement.close();
                statement = null;
            }
            connection.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                }
            }
            throw new ApplicationException(e.getMessage());
        } finally {
            close(statement);
            close(connection);
        }
    }
