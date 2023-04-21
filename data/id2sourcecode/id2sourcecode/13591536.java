    public boolean insertIllness(String illnessDocument) {
        boolean ret = false;
        PreparedStatement psRollback = null;
        try {
            helper = new DBHelper();
            IllnessDocument doc = IllnessDocument.Factory.parse(illnessDocument);
            PreparedStatement psAdd = helper.prepareStatement(SQL.insertIllness());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            psRollback = helper.prepareStatement(SQL.rollback());
            psAdd.setString(1, MedisisKeyGenerator.generate());
            psAdd.setString(2, doc.getIllness().getIllness().getIllness());
            psBegin.executeUpdate();
            psAdd.executeUpdate();
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
