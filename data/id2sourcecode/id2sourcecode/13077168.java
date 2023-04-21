    public boolean changePassword(String userDocument) {
        boolean ret = false;
        DBHelper helper = null;
        PreparedStatement psRollback = null;
        try {
            UserDocument doc = UserDocument.Factory.parse(userDocument);
            helper = new DBHelper();
            PreparedStatement psPassword = helper.prepareStatement(SQL.getUpdatePassword());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            psRollback = helper.prepareStatement(SQL.rollback());
            psPassword.setString(1, doc.getUser().getPassword());
            psPassword.setString(2, doc.getUser().getUserno());
            psPassword.executeUpdate();
            psCommit.executeUpdate();
            ret = true;
        } catch (Exception e) {
            try {
                if (psRollback != null) {
                    psRollback.executeUpdate();
                }
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (helper != null) {
                    helper.cleanup();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
