    private synchronized boolean insertResultDB(Connection connection, PreparedStatement insertResultsStatement, PreparedStatement insertFiltersStatement, PreparedStatement insertDataStatement, ActivityResult result) {
        boolean success = false;
        try {
            boolean innerSuccess = false;
            connection.setAutoCommit(false);
            insertResultsStatement.setString(1, result.getMailID());
            insertResultsStatement.setBoolean(2, result.isSpam());
            insertResultsStatement.setString(3, result.getSubject());
            insertResultsStatement.setString(4, Mail.extractEMailAddress(result.getSender(), Mail.SIMPLE_MAIL_EXTRACT_PATTERN));
            insertResultsStatement.setInt(5, result.getLastActivity().getValue());
            insertResultsStatement.setString(6, result.getActivityText());
            insertResultsStatement.setTimestamp(7, new java.sql.Timestamp(result.getTimestamp()));
            innerSuccess = 1 == insertResultsStatement.executeUpdate();
            if (result.hasAnyFilterResults()) {
                for (FilterResult filterResult : result.getFilterResults()) {
                    insertFiltersStatement.setString(1, result.getMailID());
                    insertFiltersStatement.setString(2, filterResult.getID());
                    insertFiltersStatement.setBoolean(3, filterResult.isSpam());
                    innerSuccess &= 1 == insertFiltersStatement.executeUpdate();
                }
            }
            insertDataStatement.setString(1, result.getMailID());
            DBMain.setObject(insertDataStatement, 2, result);
            innerSuccess &= 1 == insertDataStatement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            success = innerSuccess;
        } catch (Exception e) {
            LogFacility.logStackTrace(logger, Level.WARNING, e);
            try {
                connection.rollback();
            } catch (Exception e2) {
            }
        }
        return success;
    }
