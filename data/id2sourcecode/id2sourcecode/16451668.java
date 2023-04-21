    @Test
    public void test_very_bad_write_on_readonly() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), true, false);
        try {
            db.executeSql("INSERT INTO test_bug_1952 (key, value) VALUES ('key7', 'value7');");
            assertTrue(false);
        } catch (SQLException e) {
            assertTrue(true);
            assertFalse(help_check(db, "7"));
        } finally {
            db.safeClose();
        }
    }
