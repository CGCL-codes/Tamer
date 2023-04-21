    public boolean deleteIllness(String illnessDocument) {
        boolean ret = false;
        PreparedStatement psRollback = null;
        try {
            helper = new DBHelper();
            IllnessDocument doc = IllnessDocument.Factory.parse(illnessDocument);
            psRollback = helper.prepareStatement(SQL.rollback());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            PreparedStatement psDelete = helper.prepareStatement(SQL.deleteIllness());
            psDelete.setString(1, doc.getIllness().getIllness().getIllnessno());
            psBegin.executeUpdate();
            psDelete.executeUpdate();
            psCommit.executeUpdate();
            ret = true;
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
