    public String registerResource(String user, String resourceID, String sql, SqlParam[] sqlParams) throws XregistryException {
        try {
            Connection connection = globalContext.createConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement(ADD_RESOURCE_SQL);
                statement.setString(1, resourceID);
                statement.setString(2, user);
                statement.executeUpdate();
                statement = connection.prepareStatement(sql);
                for (int i = 0; i < sqlParams.length; i++) {
                    SqlParam param = sqlParams[i];
                    switch(param.getType()) {
                        case Int:
                            statement.setInt(i + 1, Integer.parseInt(param.getValue()));
                            break;
                        case String:
                            statement.setString(i + 1, param.getValue());
                            break;
                        case Long:
                            statement.setLong(i + 1, Long.parseLong(param.getValue()));
                            break;
                        default:
                            throw new XregistryException("Unknown SQL param type " + param.getType());
                    }
                }
                statement.executeUpdate();
                log.info("Execuate SQL " + statement);
                connection.commit();
                return resourceID;
            } catch (Throwable e) {
                connection.rollback();
                throw new XregistryException(e);
            } finally {
                try {
                    statement.close();
                    connection.setAutoCommit(true);
                    globalContext.closeConnection(connection);
                } catch (SQLException e) {
                    throw new XregistryException(e);
                }
            }
        } catch (SQLException e) {
            throw new XregistryException(e);
        }
    }
