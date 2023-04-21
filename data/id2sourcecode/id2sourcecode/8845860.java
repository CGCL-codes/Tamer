    public static void add(Hashtable studentInfo, String mode) throws Exception {
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            boolean found = false;
            {
                r.add("id");
                r.add("id", (String) studentInfo.get("student_id"));
                sql = r.getSQLSelect("student");
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) found = true; else found = false;
            }
            if (found && !"update".equals(mode)) throw new Exception("student Id was invalid!");
            {
                r.clear();
                r.add("applicant_id", getInfoData(studentInfo, "applicant_id"));
                r.add("password", getInfoData(studentInfo, "student_id"));
                r.add("name", getInfoData(studentInfo, "name"));
                r.add("icno", getInfoData(studentInfo, "icno"));
                r.add("address", getInfoData(studentInfo, "address"));
                r.add("address1", getInfoData(studentInfo, "address1"));
                r.add("address2", getInfoData(studentInfo, "address2"));
                r.add("address3", getInfoData(studentInfo, "address3"));
                r.add("city", getInfoData(studentInfo, "city"));
                r.add("state", getInfoData(studentInfo, "state"));
                r.add("poscode", getInfoData(studentInfo, "poscode"));
                r.add("country_code", getInfoData(studentInfo, "country_code"));
                r.add("phone", getInfoData(studentInfo, "phone"));
                r.add("birth_date", getInfoData(studentInfo, "birth_date"));
                r.add("gender", getInfoData(studentInfo, "gender"));
            }
            if (!found) {
                r.add("id", (String) studentInfo.get("student_id"));
                sql = r.getSQLInsert("student");
                stmt.executeUpdate(sql);
            } else {
                r.update("id", (String) studentInfo.get("student_id"));
                sql = r.getSQLUpdate("student");
                stmt.executeUpdate(sql);
            }
            {
                String centre_id = getInfoData(studentInfo, "centre_id");
                sql = "update student set centre_id = '" + centre_id + "' where id = '" + (String) studentInfo.get("student_id") + "'";
                stmt.executeUpdate(sql);
            }
            conn.commit();
        } catch (DbException dbex) {
            throw dbex;
        } catch (SQLException sqlex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
            throw sqlex;
        } finally {
            if (db != null) db.close();
        }
        createPortalLogin((String) studentInfo.get("student_id"), (String) studentInfo.get("student_id"), (String) studentInfo.get("name"));
    }
