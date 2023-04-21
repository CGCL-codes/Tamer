    public void execute() throws BuildException {
        validate();
        try {
            Connection connection = to.getConnection();
            StatementBatch statement = null;
            try {
                connection.createStatement().execute("create table a (id integer)");
                connection.commit();
                statement = new StatementBatch(connection.createStatement(), batchSize);
                to.writeSchemas(readSchemas(), statement);
                cleanUp(statement);
                statement.flush();
            } finally {
                if (statement != null) statement.close();
                connection.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }
