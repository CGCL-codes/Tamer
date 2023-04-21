    public boolean deleteWard(String wardDocument) {
        boolean ret = false;
        PreparedStatement psRollback = null;
        try {
            helper = new DBHelper();
            WardDocument doc = WardDocument.Factory.parse(wardDocument);
            psRollback = helper.prepareStatement(SQL.rollback());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            PreparedStatement psDelete = helper.prepareStatement(SQL.deleteWard());
            psDelete.setString(1, doc.getWard().getWard().getWardno());
            psBegin.executeUpdate();
            psDelete.executeUpdate();
            psCommit.executeUpdate();
        } catch (Exception e) {
            try {
                psRollback.executeUpdate();
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
