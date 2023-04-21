    public static void repair(String studentId) throws Exception {
        Db db = null;
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            SQLRenderer r = new SQLRenderer();
            SQLRendererAdapter ad = new SQLRendererAdapter(r);
            Hashtable h = new Hashtable();
            {
                r.add("student_id").add("course_id").add("period_root_id").add("intake_session").add("program_code").add("track_id").add("intake_code").add("intake_year").add("intake_month").add("student_id", studentId);
                ResultSet rs = ad.doSelect(db, "student_course");
                if (rs.next()) {
                    h.put("student_id", rs.getString("student_id"));
                    h.put("course_id", rs.getString("course_id"));
                    h.put("period_root_id", rs.getString("period_root_id"));
                    h.put("intake_session", rs.getString("intake_session"));
                    h.put("program_code", rs.getString("program_code"));
                    h.put("track_id", rs.getString("track_id"));
                    h.put("intake_code", rs.getString("intake_code"));
                    h.put("intake_year", new Integer(rs.getInt("intake_year")));
                    h.put("intake_month", new Integer(rs.getInt("intake_month")));
                }
            }
            {
                String sql = "delete from student_course where student_id = '" + studentId + "'";
                db.getStatement().executeUpdate(sql);
            }
            {
                r.clear();
                r.add("student_id", (String) h.get("student_id")).add("course_id", (String) h.get("course_id")).add("period_root_id", (String) h.get("period_root_id")).add("intake_session", (String) h.get("intake_session")).add("program_code", (String) h.get("program_code")).add("track_id", (String) h.get("track_id")).add("intake_code", (String) h.get("intake_code")).add("intake_year", ((Integer) h.get("intake_year")).intValue()).add("intake_month", ((Integer) h.get("intake_month")).intValue());
                ad.doInsert(db, "student_course");
            }
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException sqlex) {
            }
            throw ex;
        } finally {
            if (db != null) db.close();
        }
    }
