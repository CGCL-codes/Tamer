    private void dbSetRemoved(Enumeration eData, String uid, String image) throws Exception {
        if (eData == null) {
            return;
        }
        dbFlag.getBusyFlag();
        try {
            con.setAutoCommit(false);
            int i = 500;
            while (eData.hasMoreElements()) {
                String object = (String) eData.nextElement();
                psRemove.clearParameters();
                Timestamp st = new Timestamp(Calendar.getInstance().getTime().getTime());
                psRemove.setTimestamp(1, st);
                psRemove.setString(2, uid);
                psRemove.setString(3, image);
                psRemove.setString(4, object);
                int rows = psRemove.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            while (e != null) {
                System.err.println("state:" + e.getSQLState() + "\ncode:" + e.getErrorCode() + "\nstring:" + e.toString() + "\nstack:");
                e.printStackTrace();
                e = e.getNextException();
            }
            try {
                con.rollback();
            } catch (Exception ex) {
                con = null;
            }
            dbFlag.freeBusyFlag();
            throw (new Exception(e.toString()));
        } catch (Exception e) {
            dbFlag.freeBusyFlag();
            e.printStackTrace();
            try {
                con.rollback();
            } catch (Exception ex) {
                con = null;
            }
            throw (new Exception(e.toString()));
        }
        try {
            con.setAutoCommit(true);
        } catch (Exception e) {
        }
        dbFlag.freeBusyFlag();
    }
