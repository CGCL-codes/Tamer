    public void onServiceStartedEvent(ServiceStartedEvent event, ActivityContextInterface aci) {
        tracer.info("JDBC Example started.");
        JdbcActivity jdbcActivity = jdbcRA.createActivity();
        tracer.info("Created JDBC RA activity, using RA's SBB Interface.");
        ActivityContextInterface jdbcACI = jdbcACIF.getActivityContextInterface(jdbcActivity);
        jdbcACI.attach(contextExt.getSbbLocalObject());
        tracer.info("Retrieved the ACI related to the JDBC RA activity, and attached the sbb entity.");
        SimpleJdbcTask task = new SimpleJdbcTask() {

            @Override
            public Object executeSimple(JdbcTaskContext context) {
                SleeTransaction tx = null;
                try {
                    tx = context.getSleeTransactionManager().beginSleeTransaction();
                    Connection connection = context.getConnection();
                    Statement statement = connection.createStatement();
                    tracer.info("Created statement to create table, executing query...");
                    statement.execute("CREATE TABLE TestTable (Name VARCHAR(30));");
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TestTable VALUES(?)");
                    preparedStatement.setString(1, "Mobicents");
                    tracer.info("Created prepared statement for data insert, executing...");
                    preparedStatement.execute();
                    preparedStatement = connection.prepareStatement("SELECT ? From TestTable;");
                    preparedStatement.setString(1, "Name");
                    tracer.info("Created prepared statement for data query, executing...");
                    preparedStatement.execute();
                    ResultSet resultSet = preparedStatement.getResultSet();
                    resultSet.next();
                    tracer.info("Data query first result: " + resultSet.getString(1));
                    Statement anotherStatement = connection.createStatement();
                    tracer.info("Created statement to drop table, executing update...");
                    anotherStatement.executeUpdate("DROP TABLE TestTable;");
                    tx.commit();
                    tx = null;
                    return true;
                } catch (Exception e) {
                    tracer.severe("failed to create table", e);
                    if (tx != null) {
                        try {
                            tx.rollback();
                        } catch (Exception f) {
                            tracer.severe("failed to rollback tx", f);
                        }
                    }
                    return false;
                }
            }
        };
        jdbcActivity.execute(task);
    }
