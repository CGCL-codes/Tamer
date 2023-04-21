    public boolean updateUser(String userDocument) {
        boolean ret = false;
        DBHelper helper = null;
        PreparedStatement psRollback = null;
        try {
            helper = new DBHelper();
            UserDocument doc = UserDocument.Factory.parse(userDocument);
            psRollback = helper.prepareStatement(SQL.rollback());
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psUpdate = helper.prepareStatement(SQL.updateUser());
            psUpdate.setString(1, doc.getUser().getRoleno());
            psUpdate.setString(2, doc.getUser().getPassword());
            psUpdate.setString(3, doc.getUser().getTitle());
            psUpdate.setString(4, doc.getUser().getName());
            psUpdate.setString(5, doc.getUser().getSurname());
            psUpdate.setString(6, doc.getUser().getTelephone());
            psUpdate.setString(7, doc.getUser().getCellphone());
            psUpdate.setString(8, doc.getUser().getHospitalno());
            psUpdate.setString(9, doc.getUser().getUserno());
            psBegin.executeUpdate();
            psUpdate.executeUpdate();
            psCommit.executeUpdate();
            ret = true;
        } catch (Exception e) {
            ret = false;
            try {
                if (psRollback != null) {
                    psRollback.executeUpdate();
                }
            } catch (Exception ee) {
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
