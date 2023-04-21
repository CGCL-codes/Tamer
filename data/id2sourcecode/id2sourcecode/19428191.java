    public void removeDocument(String documentId) {
        logger.debug("removeDocument(%s)", documentId);
        Connection connection = null;
        NamedParameterStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA));
            statement.setString(QueryStore.DELETE_DOCUMENT_PREVIEW_DATA_PARAM_ID_DOCUMENT, documentId);
            statement.executeUpdate();
            statement.close();
            statement = null;
            statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT_DATA));
            statement.setString(QueryStore.DELETE_DOCUMENT_DATA_ID_DOCUMENT, documentId);
            statement.executeUpdate();
            statement.close();
            statement = null;
            statement = new NamedParameterStatement(connection, this.queryStore.get(QueryStore.DELETE_DOCUMENT));
            statement.setString(QueryStore.DELETE_DOCUMENT_ID_DOCUMENT, documentId);
            statement.executeUpdate();
            statement.close();
            statement = null;
            connection.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e1) {
                logger.error(e.getMessage(), e1);
            }
        } finally {
            close(statement);
            close(connection);
        }
    }
