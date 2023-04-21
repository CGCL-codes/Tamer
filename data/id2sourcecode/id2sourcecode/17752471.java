    public void testSavepoint() throws Exception {
        DatabaseMetaData dbmd = this.conn.getMetaData();
        if (dbmd.supportsSavepoints()) {
            System.out.println("Testing SAVEPOINTs");
            try {
                this.conn.setAutoCommit(true);
                this.stmt.executeUpdate("DROP TABLE IF EXISTS testSavepoints");
                this.stmt.executeUpdate("CREATE TABLE testSavepoints (field1 int) TYPE=InnoDB");
                this.conn.setAutoCommit(false);
                this.stmt.executeUpdate("INSERT INTO testSavepoints VALUES (1)");
                Savepoint afterInsert = this.conn.setSavepoint("afterInsert");
                this.stmt.executeUpdate("UPDATE testSavepoints SET field1=2");
                Savepoint afterUpdate = this.conn.setSavepoint("afterUpdate");
                this.stmt.executeUpdate("DELETE FROM testSavepoints");
                assertTrue("Row count should be 0", getRowCount("testSavepoints") == 0);
                this.conn.rollback(afterUpdate);
                assertTrue("Row count should be 1", getRowCount("testSavepoints") == 1);
                assertTrue("Value should be 2", "2".equals(getSingleValue("testSavepoints", "field1", null).toString()));
                this.conn.rollback(afterInsert);
                assertTrue("Value should be 1", "1".equals(getSingleValue("testSavepoints", "field1", null).toString()));
                this.conn.rollback();
                assertTrue("Row count should be 0", getRowCount("testSavepoints") == 0);
                this.conn.rollback();
                this.stmt.executeUpdate("INSERT INTO testSavepoints VALUES (1)");
                afterInsert = this.conn.setSavepoint();
                this.stmt.executeUpdate("UPDATE testSavepoints SET field1=2");
                afterUpdate = this.conn.setSavepoint();
                this.stmt.executeUpdate("DELETE FROM testSavepoints");
                assertTrue("Row count should be 0", getRowCount("testSavepoints") == 0);
                this.conn.rollback(afterUpdate);
                assertTrue("Row count should be 1", getRowCount("testSavepoints") == 1);
                assertTrue("Value should be 2", "2".equals(getSingleValue("testSavepoints", "field1", null).toString()));
                this.conn.rollback(afterInsert);
                assertTrue("Value should be 1", "1".equals(getSingleValue("testSavepoints", "field1", null).toString()));
                this.conn.rollback();
                this.conn.releaseSavepoint(this.conn.setSavepoint());
            } finally {
                this.conn.setAutoCommit(true);
                this.stmt.executeUpdate("DROP TABLE IF EXISTS testSavepoints");
            }
        } else {
            System.out.println("MySQL version does not support SAVEPOINTs");
        }
    }
