    public int updateDirectoryDetails(String table_name, String dir_id, String dir_name, String dir_type, String dir_level, String dir_age, String pre_level, String pre_age, String read_level, String read_age, String write_level, String write_age, String managers, String admins) {
        int rs = 0;
        if (table_name == null || dir_id == null) {
            return 0;
        }
        if (managers == null) {
            managers = "";
        }
        if (admins == null) {
            admins = "";
        }
        try {
            makeConnection();
            String query = "UPDATE ? SET TYPE=?, NAME=?, DIRECTORY_LEVEL=?, DIRECTORY_AGE=?," + " PREVIEW_LEVEL=?, PREVIEW_AGE=?, READ_LEVEL=?, READ_AGE=?, WRITE_LEVEL=?, WRITE_AGE=?," + " MANAGERS=?, ADMINS=? WHERE ID=?;";
            PreparedStatement prepStmt = con.prepareStatement(query);
            prepStmt.setString(1, table_name);
            prepStmt.setString(2, dir_type);
            prepStmt.setString(3, dir_name);
            prepStmt.setString(4, dir_level);
            prepStmt.setString(5, dir_age);
            prepStmt.setString(6, pre_level);
            prepStmt.setString(7, pre_age);
            prepStmt.setString(8, read_level);
            prepStmt.setString(9, read_age);
            prepStmt.setString(10, write_level);
            prepStmt.setString(11, write_age);
            prepStmt.setString(12, managers);
            prepStmt.setString(13, admins);
            prepStmt.setString(14, dir_id);
            rs = prepStmt.executeUpdate();
            prepStmt.close();
            releaseConnection();
        } catch (Exception e) {
            releaseConnection();
            System.out.println("EJB:DirectoryBean: updateDirectoryDetails error:" + e.getMessage());
            rs = -1;
        }
        return rs;
    }
