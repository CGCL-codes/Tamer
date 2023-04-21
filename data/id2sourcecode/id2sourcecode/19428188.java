    public void createDocument(String documentId, String templateId, int state) {
        logger.debug("createDocument(%s, %s, %s)", documentId, templateId, String.valueOf(state));
        Connection connection = null;
        NamedParameterStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.INSERT_DOCUMENT));
            statement.setString(QueryStore.INSERT_DOCUMENT_PARAM_ID, documentId);
            statement.setString(QueryStore.INSERT_DOCUMENT_PARAM_ID_TEMPLATE, templateId);
            statement.setInt(QueryStore.INSERT_DOCUMENT_PARAM_STATE, state);
            statement.executeUpdate();
            statement.close();
            statement = null;
            statement = new NamedParameterStatement(connection, queryStore.get(QueryStore.CREATE_DOCUMENT_DATA_FROM_TEMPLATE));
            statement.setString(QueryStore.CREATE_DOCUMENT_DATA_FROM_TEMPLATE_PARAM_ID_DOCUMENT, documentId);
            statement.executeUpdate();
            statement.close();
            statement = null;
            connection.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error(e.getMessage(), e);
            }
            throw new ApplicationException(e.getMessage());
        } finally {
            close(statement);
            close(connection);
        }
    }
