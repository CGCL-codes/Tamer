    public boolean update(String description, String text, Date insertDate) throws SQLException {
        if (conn == null) {
            throw new IllegalStateException("You must first call initialize()!");
        }
        if (!alreadyApplied(text)) {
            PreparedStatement pstmt = null;
            md.reset();
            md.update(text.getBytes());
            try {
                pstmt = conn.prepareStatement("insert into " + tableName + "(insert_date , sqltext, description, sqltext_hash) values(?, ?,  ?, ?) ");
                pstmt.setDate(1, insertDate);
                pstmt.setString(2, text);
                pstmt.setString(3, description);
                pstmt.setString(4, new String(md.digest()));
                pstmt.executeUpdate();
            } finally {
                DBUtil.close(pstmt);
            }
            existingCommands.add(text);
            return true;
        }
        return false;
    }
