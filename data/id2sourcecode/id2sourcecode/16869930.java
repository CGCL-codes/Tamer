    public static void doInsertTask(String user, String subjectid, Hashtable data) throws Exception {
        if (subjectid == null) subjectid = "";
        Db db = null;
        Connection conn = null;
        String sql = "";
        String task_description = (String) data.get("description");
        if ("".equals(task_description.trim())) return;
        String year1 = (String) data.get("year1");
        String month1 = (String) data.get("month1");
        String day1 = (String) data.get("day1");
        String hour1 = (String) data.get("hour1");
        String minute1 = (String) data.get("minute1");
        String hour2 = (String) data.get("hour2");
        String minute2 = (String) data.get("minute2");
        String invitelist = (String) data.get("invitelist");
        int ispublic = 1;
        String task_date = year1 + "-" + fmt(month1) + "-" + fmt(day1);
        Vector inviteVector = new Vector();
        StringTokenizer tokenizer = new StringTokenizer(invitelist);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token != null) {
                inviteVector.addElement(token.trim());
            }
        }
        String id = user.concat(Long.toString(UniqueID.get()));
        try {
            db = new Db();
            conn = db.getConnection();
            Statement stmt = db.getStatement();
            conn.setAutoCommit(false);
            SQLRenderer r = new SQLRenderer();
            {
                r.add("task_id", id);
                r.add("user_login", user);
                r.add("task_description", task_description);
                r.add("task_date", task_date);
                r.add("hour_start", Integer.parseInt(hour1));
                r.add("hour_end", Integer.parseInt(hour2));
                r.add("minute_start", Integer.parseInt(minute1));
                r.add("minute_end", Integer.parseInt(minute2));
                r.add("task_public", ispublic);
                r.add("subject_id", subjectid);
                sql = r.getSQLInsert("planner_task");
                stmt.executeUpdate(sql);
            }
            {
                sql = "DELETE FROM planner_task_invite WHERE task_id = '" + id + "' ";
                stmt.executeUpdate(sql);
            }
            for (int i = 0; i < inviteVector.size(); i++) {
                r = new SQLRenderer();
                r.add("task_id", id);
                r.add("user_id", (String) inviteVector.elementAt(i));
                r.add("inviter_id", user);
                r.add("allow_edit", 0);
                sql = r.getSQLInsert("planner_task_invite");
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (DbException dbex) {
            System.out.println(dbex.getMessage());
            throw dbex;
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rex) {
                }
            }
            System.out.println(ex.getMessage() + sql);
            throw ex;
        } finally {
            if (db != null) db.close();
        }
    }
