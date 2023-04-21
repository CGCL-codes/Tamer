    public boolean addUser(String userDocument) {
        boolean ret = true;
        DBHelper helper = null;
        PreparedStatement psRollback = null;
        try {
            helper = new DBHelper();
            UserDocument doc = UserDocument.Factory.parse(userDocument);
            psRollback = helper.prepareStatement(SQL.rollback());
            PreparedStatement psBegin = helper.prepareStatement(SQL.begin());
            PreparedStatement psCommit = helper.prepareStatement(SQL.commit());
            PreparedStatement psAdd = helper.prepareStatement(SQL.insertUser());
            psAdd.setString(1, doc.getUser().getHospitalno());
            psAdd.setString(2, MedisisKeyGenerator.generate());
            psAdd.setString(3, doc.getUser().getRoleno());
            psAdd.setString(4, doc.getUser().getUsername());
            psAdd.setString(5, doc.getUser().getPassword());
            psAdd.setString(6, doc.getUser().getTitle());
            psAdd.setString(7, doc.getUser().getName());
            psAdd.setString(8, doc.getUser().getSurname());
            psAdd.setString(9, doc.getUser().getTelephone());
            psAdd.setString(10, doc.getUser().getCellphone());
            psBegin.executeUpdate();
            psAdd.executeUpdate();
            psCommit.executeUpdate();
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
