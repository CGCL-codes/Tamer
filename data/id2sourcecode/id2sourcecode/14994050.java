    public static boolean change(String oldId, String newId) throws Exception {
        String sql = "";
        Db db = null;
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            Statement stmt = db.getStatement();
            sql = "select id from student where id = '" + newId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return false;
            }
            sql = "update learner_sco set member_id = '" + newId + "' where member_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update quiz_assessment_answer set user_id = '" + newId + "' where user_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update quiz_assessment_attempt set user_id = '" + newId + "' where user_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update quiz_assessment_result set user_id = '" + newId + "' where user_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student set id = '" + newId + "' where id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_billing set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_billing_detail set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_receipt set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_receipt_detail set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_centre set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_course set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_deposit set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_status set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update student_subject set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update transcript_display set student_id = '" + newId + "' where student_id = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update users set user_login = '" + newId + "' where user_login = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update tabs set user_login = '" + newId + "' where user_login = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update user_module set user_login = '" + newId + "' where user_login = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update user_css set user_login = '" + newId + "' where user_login = '" + oldId + "'";
            stmt.executeUpdate(sql);
            sql = "update user_tracker set user_login = '" + newId + "' where user_login = '" + oldId + "'";
            stmt.executeUpdate(sql);
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException er) {
            }
            throw e;
        } finally {
            if (db != null) db.close();
        }
    }
