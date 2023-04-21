    private void doUpdateTaskX(HttpSession session) {
        Db db = null;
        Connection conn = null;
        String sql = "";
        String user = (String) session.getAttribute("_portal_login");
        String id = getParam("task_id");
        String task_description = getParam("description");
        String year1 = getParam("year1");
        String month1 = getParam("month1");
        String day1 = getParam("day1");
        String hour1 = getParam("hour1");
        String minute1 = getParam("minute1");
        String hour2 = getParam("hour2");
        String minute2 = getParam("minute2");
        int ispublic = !"".equals(getParam("public")) ? Integer.parseInt(getParam("public")) : 1;
        String task_date = year1 + "-" + fmt(month1) + "-" + fmt(day1);
        String[] invitelist = request.getParameterValues("invitelist");
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            conn.setAutoCommit(false);
            SQLRenderer r = new SQLRenderer();
            {
                sql = "UPDATE planner_task SET task_description = '" + task_description + "', " + "task_date = '" + task_date + " WHERE task_id = '" + id + "'";
                stmt.executeUpdate(sql);
            }
            {
                sql = "DELETE FROM planner_task_invite WHERE task_id = '" + id + "' ";
                stmt.executeUpdate(sql);
            }
            if (invitelist != null) {
                for (int i = 0; i < invitelist.length; i++) {
                    r = new SQLRenderer();
                    r.add("task_id", id);
                    r.add("user_id", invitelist[i]);
                    r.add("inviter_id", user);
                    r.add("allow_edit", 0);
                    sql = r.getSQLInsert("planner_task_invite");
                    stmt.executeUpdate(sql);
                }
            }
            conn.commit();
        } catch (DbException dbex) {
            System.out.println(dbex.getMessage());
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rex) {
                }
            }
            System.out.println(ex.getMessage() + sql);
        } finally {
            if (db != null) db.close();
        }
    }
