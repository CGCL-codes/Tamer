    public boolean insertWard(String wardDocument) {
        boolean ret = false;
        PreparedStatement psRollback = null;
        try {
            helper = new DBHelper();
            WardDocument doc = WardDocument.Factory.parse(wardDocument);
            PreparedStatement psAdd = helper.prepareStatement(SQL.insertWard());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            psRollback = helper.prepareStatement(SQL.rollback());
            psAdd.setString(1, doc.getWard().getWard().getHospitalno());
            psAdd.setString(2, MedisisKeyGenerator.generate());
            psAdd.setString(3, doc.getWard().getWard().getWard());
            psAdd.setString(4, doc.getWard().getWard().getWardDescription());
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
