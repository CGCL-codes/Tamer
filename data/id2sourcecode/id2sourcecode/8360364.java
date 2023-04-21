    public static void deleteLanguage(String LanguageLabel) {
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            theConnection.setAutoCommit(false);
            String query = "DELETE FROM tr_translation WHERE tr_translation_language =?";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, LanguageLabel);
            state.executeUpdate();
            String query2 = "DELETE FROM tr_language WHERE tr_language_label=?";
            PreparedStatement state2 = theConnection.prepareStatement(query2);
            state2.setString(1, LanguageLabel);
            state2.executeUpdate();
            theConnection.commit();
        } catch (SQLException e) {
            try {
                theConnection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
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
