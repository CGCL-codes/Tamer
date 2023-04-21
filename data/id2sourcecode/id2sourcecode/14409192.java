    public boolean transaction(Long personId) {
        boolean result = true;
        String delPets = "delete from PETS where PERSON_ID = ?";
        String delPersons = "delete from PERSONS where PERSON_ID = ?";
        if (true) {
            System.out.println(delPets);
            System.out.println(delPersons);
        }
        Connection conn = null;
        PreparedStatement pstmtDelPets = null;
        PreparedStatement pstmtDelPersons = null;
        try {
            conn = ConnHelper.getConnectionByDriverManager();
            conn.setAutoCommit(false);
            pstmtDelPets = conn.prepareStatement(delPets);
            pstmtDelPets.setLong(1, personId);
            int affectedRows = pstmtDelPets.executeUpdate();
            pstmtDelPets.close();
            System.out.println("affectedRows = " + affectedRows);
            if (true) {
                throw new SQLException("fasfdsaf");
            }
            pstmtDelPersons = conn.prepareStatement(delPersons);
            pstmtDelPersons.setLong(1, personId);
            affectedRows = pstmtDelPersons.executeUpdate();
            System.out.println("affectedRows = " + affectedRows);
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e.printStackTrace(System.out);
            }
            e.printStackTrace(System.out);
            result = false;
        } finally {
            ConnHelper.close(conn, pstmtDelPersons, null);
        }
        return result;
    }
