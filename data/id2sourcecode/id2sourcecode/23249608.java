        int doOne(int bid, int tid, int aid, int delta) {
            int aBalance = 0;
            if (Conn == null) {
                incrementFailedTransactionCount();
                return 0;
            }
            try {
                pstmt1.setInt(1, delta);
                pstmt1.setInt(2, aid);
                pstmt1.executeUpdate();
                pstmt1.clearWarnings();
                pstmt2.setInt(1, aid);
                ResultSet RS = pstmt2.executeQuery();
                pstmt2.clearWarnings();
                while (RS.next()) {
                    aBalance = RS.getInt(1);
                }
                pstmt3.setInt(1, delta);
                pstmt3.setInt(2, tid);
                pstmt3.executeUpdate();
                pstmt3.clearWarnings();
                pstmt4.setInt(1, delta);
                pstmt4.setInt(2, bid);
                pstmt4.executeUpdate();
                pstmt4.clearWarnings();
                pstmt5.setInt(1, tid);
                pstmt5.setInt(2, bid);
                pstmt5.setInt(3, aid);
                pstmt5.setInt(4, delta);
                pstmt5.executeUpdate();
                pstmt5.clearWarnings();
                Conn.commit();
                return aBalance;
            } catch (Exception E) {
                if (verbose) {
                    System.out.println("Transaction failed: " + E.getMessage());
                    E.printStackTrace();
                }
                incrementFailedTransactionCount();
                try {
                    Conn.rollback();
                } catch (SQLException E1) {
                }
            }
            return 0;
        }
