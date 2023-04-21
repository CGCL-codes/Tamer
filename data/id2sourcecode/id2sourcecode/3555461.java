    public String deleteCategory(String categoryname, String userid) {
        String res = MediaErrorMessages.ERROR_DATABASE;
        if (categoryname != null && !categoryname.trim().equals("") && userid != null && !userid.trim().equals("")) {
            try {
                char c = 39;
                categoryname = MediaToolBox.replace(categoryname, (new Character(c)).toString(), "&#39;");
                theConnection = DBServiceManager.allocateConnection(database);
                theConnection.setAutoCommit(false);
                String query = "DELETE FROM categories ";
                query += "WHERE category='" + categoryname + "' ";
                query += "AND gallery='user_gallery' ";
                query += "AND user_id='" + userid + "'";
                PreparedStatement state = theConnection.prepareStatement(query);
                state.executeUpdate();
                query = "DELETE FROM user_gallery ";
                query += "WHERE category='" + categoryname + "' ";
                query += "AND user_id='" + userid + "'";
                PreparedStatement state2 = theConnection.prepareStatement(query);
                state2.executeUpdate();
                theConnection.commit();
                res = MediaErrorMessages.ACTION_DONE;
            } catch (SQLException e) {
                if (theConnection != null) {
                    try {
                        theConnection.rollback();
                    } catch (SQLException ex) {
                    }
                }
                res = MediaErrorMessages.ERROR_DATABASE;
            } finally {
                if (theConnection != null) {
                    try {
                        theConnection.setAutoCommit(true);
                    } catch (SQLException ex) {
                    }
                    theConnection.release();
                }
            }
        } else {
            return MediaErrorMessages.EMPTY_PARAM;
        }
        return res;
    }
