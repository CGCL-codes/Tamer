    public void editAssets(HttpServletRequest request) {
        Assets ass = new Assets();
        ass.id = StrFun.getInt(request, "id");
        ass.assetsName = StrFun.getString(request, "assetsName");
        ass.changeRate = StrFun.getInt(request, "changeRate");
        ass.updateAmount = StrFun.getInt(request, "updateAmount");
        ass.changePrice = StrFun.getInt(request, "changePrice");
        String changeRemark = StrFun.getString(request, "changeRemark");
        ass.updateDate = new Date();
        DBConnect dbc = null;
        Connection conn = null;
        try {
            dbc = DBConnect.createDBConnect();
            conn = dbc.getConnection();
            conn.setAutoCommit(false);
            String sql = "update SS_Assets set " + "assetsName='" + ass.assetsName + "', " + "changeRate='" + ass.changeRate + "', " + "updateAmount='" + ass.updateAmount + "', " + "changePrice='" + ass.changePrice + "', " + "updateDate='" + GetDate.dateToStr(ass.updateDate) + "' " + "where id=" + ass.id;
            conn.createStatement().executeUpdate(sql);
            sql = "insert into SS_AssetsLine (aid, changeAmount, changeNum, changePrice, changeRemark, userNo) values(" + "'" + ass.id + "', " + "'" + ass.updateAmount + "', " + "'" + ass.updateAmount + "', " + "'" + ass.changePrice + "', " + "'" + changeRemark + "', " + "'" + ServerUtil.getUserFromSession(request).userNo + "')";
            conn.createStatement().executeUpdate(sql);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            if (dbc != null) try {
                dbc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
