    public static void save(Vector examInfo, Hashtable info) throws Exception {
        String applicant_id = (String) info.get("applicant_id");
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            sql = "DELETE FROM adm_applicant_exam WHERE applicant_id = '" + applicant_id + "'";
            stmt.executeUpdate(sql);
            for (int i = 0; i < examInfo.size(); i++) {
                Hashtable exam = (Hashtable) examInfo.elementAt(i);
                String exam_id = (String) exam.get("id");
                Vector subjects = (Vector) exam.get("subjects");
                for (int k = 0; k < subjects.size(); k++) {
                    Hashtable subject = (Hashtable) subjects.elementAt(k);
                    String subject_id = (String) subject.get("id");
                    String grade = (String) info.get(subject_id);
                    if (!"".equals(grade) && !"0".equals(grade)) {
                        r.clear();
                        r.add("applicant_id", applicant_id);
                        r.add("adm_exam_id", exam_id);
                        r.add("adm_subject_id", subject_id);
                        r.add("adm_subject_grade", Integer.parseInt(grade));
                        sql = r.getSQLInsert("adm_applicant_exam");
                        stmt.executeUpdate(sql);
                    }
                }
            }
            conn.commit();
        } catch (SQLException sqlex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
        } finally {
            if (db != null) db.close();
        }
    }
