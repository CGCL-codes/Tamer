    public void testLocalTransaction() throws Exception {
        if (!versionMeetsMinimum(5, 0) || isRunningOnJdk131()) {
            return;
        }
        createTable("testLocalTransaction", "(field1 int) ENGINE=InnoDB");
        Connection conn1 = null;
        XAConnection xaConn1 = null;
        try {
            xaConn1 = getXAConnection();
            XAResource xaRes1 = xaConn1.getXAResource();
            conn1 = xaConn1.getConnection();
            assertEquals(true, conn1.getAutoCommit());
            conn1.setAutoCommit(true);
            conn1.createStatement().executeUpdate("INSERT INTO testLocalTransaction VALUES (1)");
            assertEquals("1", getSingleIndexedValueWithQuery(conn1, 1, "SELECT field1 FROM testLocalTransaction").toString());
            conn1.createStatement().executeUpdate("TRUNCATE TABLE testLocalTransaction");
            conn1.setAutoCommit(false);
            conn1.createStatement().executeUpdate("INSERT INTO testLocalTransaction VALUES (2)");
            assertEquals("2", getSingleIndexedValueWithQuery(conn1, 1, "SELECT field1 FROM testLocalTransaction").toString());
            conn1.rollback();
            assertEquals(0, getRowCount("testLocalTransaction"));
            conn1.createStatement().executeUpdate("INSERT INTO testLocalTransaction VALUES (3)");
            assertEquals("3", getSingleIndexedValueWithQuery(conn1, 1, "SELECT field1 FROM testLocalTransaction").toString());
            conn1.commit();
            assertEquals("3", getSingleIndexedValueWithQuery(conn1, 1, "SELECT field1 FROM testLocalTransaction").toString());
            conn1.commit();
            Savepoint sp = conn1.setSavepoint();
            conn1.rollback(sp);
            sp = conn1.setSavepoint("abcd");
            conn1.rollback(sp);
            Savepoint spSaved = sp;
            Xid xid = createXid();
            xaRes1.start(xid, XAResource.TMNOFLAGS);
            try {
                try {
                    conn1.setAutoCommit(true);
                } catch (SQLException sqlEx) {
                    assertEquals("2D000", sqlEx.getSQLState());
                }
                try {
                    conn1.commit();
                } catch (SQLException sqlEx) {
                    assertEquals("2D000", sqlEx.getSQLState());
                }
                try {
                    conn1.rollback();
                } catch (SQLException sqlEx) {
                    assertEquals("2D000", sqlEx.getSQLState());
                }
                try {
                    sp = conn1.setSavepoint();
                } catch (SQLException sqlEx) {
                    assertEquals("2D000", sqlEx.getSQLState());
                }
                try {
                    conn1.rollback(spSaved);
                } catch (SQLException sqlEx) {
                    assertEquals("2D000", sqlEx.getSQLState());
                }
                try {
                    sp = conn1.setSavepoint("abcd");
                } catch (SQLException sqlEx) {
                    assertEquals("2D000", sqlEx.getSQLState());
                }
                try {
                    conn1.rollback(spSaved);
                } catch (SQLException sqlEx) {
                    assertEquals("2D000", sqlEx.getSQLState());
                }
            } finally {
                xaRes1.forget(xid);
            }
        } finally {
            if (xaConn1 != null) {
                xaConn1.close();
            }
        }
    }
