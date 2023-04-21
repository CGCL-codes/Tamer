    public static void deleteApplication(String applicationExtension) {
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            theConnection.setAutoCommit(false);
            String query = "DELETE FROM tr_translation WHERE tr_translation_trtagid IN (SELECT tr_tag_id FROM tr_tag WHERE tr_tag_applicationid=?)";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, applicationExtension);
            state.executeUpdate();
            String query2 = "DELETE FROM tr_tag WHERE tr_tag_applicationid=?";
            PreparedStatement state2 = theConnection.prepareStatement(query2);
            state2.setString(1, applicationExtension);
            state2.executeUpdate();
            String query3 = "DELETE FROM tr_application WHERE tr_application_id=?";
            PreparedStatement state3 = theConnection.prepareStatement(query3);
            state3.setString(1, applicationExtension);
            state3.executeUpdate();
            theConnection.commit();
        } catch (SQLException e) {
            try {
                theConnection.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            if (theConnection != null) {
                try {
                    theConnection.setAutoCommit(true);
                } catch (SQLException ex) {
                }
                theConnection.release();
            }
        }
    }
