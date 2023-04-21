    public boolean runAutomation(Automation auto) throws SQLException {
        boolean actionSuccessful = false;
        String actionSQL = VacuumdConfigFactory.getInstance().getAction(auto.getActionName()).getStatement().getContent();
        Collection actionColumns = getTokenizedColumns(actionSQL);
        DbConnectionFactory dcf = DatabaseConnectionFactory.getInstance();
        Connection conn = null;
        Statement triggerStatement = null;
        ResultSet triggerResultSet = null;
        int resultRows = 0;
        try {
            conn = dcf.getConnection();
            triggerStatement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            MockUtil.println("trigger statement: " + VacuumdConfigFactory.getInstance().getTrigger(auto.getTriggerName()).getStatement().getContent());
            triggerResultSet = triggerStatement.executeQuery(VacuumdConfigFactory.getInstance().getTrigger(auto.getTriggerName()).getStatement().getContent());
            resultRows = countRows(triggerResultSet);
            if (resultRows < 1) return actionSuccessful;
            int triggerRowCount = VacuumdConfigFactory.getInstance().getTrigger(auto.getTriggerName()).getRowCount();
            String triggerOperator = VacuumdConfigFactory.getInstance().getTrigger(auto.getTriggerName()).getOperator();
            if (!triggerRowCheck(triggerRowCount, triggerOperator, resultRows)) return actionSuccessful;
            if (!resultSetHasRequiredActionColumns(triggerResultSet, actionColumns)) return actionSuccessful;
            PreparedStatement actionStatement = null;
            try {
                triggerResultSet.beforeFirst();
                conn.setAutoCommit(false);
                while (triggerResultSet.next()) {
                    actionStatement = convertActionToPreparedStatement(triggerResultSet, actionSQL, conn);
                    actionStatement.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                MockUtil.println("Cleaning up action statement.");
                actionStatement.close();
            }
            actionSuccessful = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MockUtil.println("Cleaning up resultset.");
            triggerResultSet.close();
            MockUtil.println("Cleaning up trigger statement.");
            triggerStatement.close();
            MockUtil.println("Cleaning up database connection.");
            conn.close();
        }
        return actionSuccessful;
    }
