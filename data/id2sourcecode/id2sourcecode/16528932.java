    public IndividualState cleanVO(String voId) {
        IndividualState cleanState = new IndividualState();
        cleanState.setServiceId(voId);
        Connection con = null;
        PreparedStatement pStmt = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            String sql = "";
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    sql = "SELECT USER_ID FROM USER_ASSIGNMENT WHERE VO_ID = ?";
                } else {
                    sql = "SELECT SP_ID FROM SP_ASSIGNMENT WHERE VO_ID = ?";
                }
                pStmt = con.prepareStatement(sql);
                pStmt.setString(1, voId);
                ResultSet rs = pStmt.executeQuery();
                ServiceProviderDao spDao = DaoFactory.getDaoFactory().getServiceProviderDao();
                UserDao userDao = DaoFactory.getDaoFactory().getUserDao();
                List<String> idList = new ArrayList<String>();
                while (rs.next()) {
                    String id = null;
                    if (i == 0) {
                        id = rs.getString("USER_ID");
                    } else {
                        id = rs.getString("SP_ID");
                    }
                    idList.add(id);
                }
                if (i == 0) {
                    userDao.cleanUserProfile(idList.toArray(new String[idList.size()]));
                } else {
                    spDao.cleanServiceProvider(idList.toArray(new String[idList.size()]));
                }
            }
            for (int i = 0; i < 3; i++) {
                switch(i) {
                    case 0:
                        sql = "DELETE FROM USER_ASSIGNMENT WHERE VO_ID = ?";
                        break;
                    case 1:
                        sql = "DELETE FROM SP_ASSIGNMENT WHERE VO_ID = ?";
                        break;
                    case 2:
                        sql = "DELETE FROM VO WHERE VO_ID = ?";
                        break;
                }
                pStmt = con.prepareStatement(sql);
                pStmt.setString(1, voId);
                int count = pStmt.executeUpdate();
                if (i == 2) {
                    if (count != 0) {
                        cleanState.setState(true);
                        con.commit();
                    } else {
                        cleanState.setFailureReason(Constants.VO_NOT_FOUND);
                        con.rollback();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error cleaning VO " + voId, e);
        } finally {
            DBUtil.closeStatement(pStmt);
            DBUtil.returnConnection(con);
        }
        return cleanState;
    }
