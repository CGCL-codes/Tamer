    public void persistCommandHistory() {
        if (!((Boolean) EXQLModuleManager.instance().getPreference(EXQLConstants.RDBMS_OPTION_REMEMBER_COMMAND_HISTORY).getValue()).booleanValue()) return;
        int maxRows = ((Integer) EXQLModuleManager.instance().getPreference(EXQLConstants.RDBMS_OPTION_MAXCOMMANDSPERSISTED).getValue()).intValue();
        List persistMe = null;
        if (commandVector.size() > maxRows) {
            persistMe = commandVector.subList(commandVector.size() - maxRows, commandVector.size());
        } else {
            persistMe = commandVector;
        }
        log.debug("Persisting");
        PreparedStatement st = null;
        Connection conn = null;
        Transaction t = null;
        try {
            conn = DbStoreModuleManager.instance().getConnection();
            t = new Transaction(conn);
            st = conn.prepareStatement("DELETE FROM SQL_COMMAND_HISTORY WHERE CONNECTION_NAME=?");
            st.setString(1, connectionName);
            st.executeUpdate();
            st.close();
            for (int i = 0; i < persistMe.size(); i++) {
                st = conn.prepareStatement("INSERT INTO SQL_COMMAND_HISTORY (ID, CONNECTION_NAME, COMMAND) VALUES (NULL,?,?)");
                st.setString(1, connectionName);
                st.setString(2, (String) persistMe.get(i));
                st.executeUpdate();
                st.close();
            }
            t.commit();
        } catch (SQLException e) {
            if (t != null) {
                t.rollback();
            }
            ExceptionManagerFactory.getExceptionManager().manageException(e, "Exception caught while saving command history");
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e1) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                }
            }
        }
    }
