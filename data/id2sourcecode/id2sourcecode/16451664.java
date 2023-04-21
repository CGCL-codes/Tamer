    @Test
    public void test_bad_write_on_readonly() throws Exception {
        Db db = new Db(null, null, DbConnection.oldDatabaseConnection(), true, false);
        try {
            help_write(db, "3", false);
            assertTrue(false);
        } catch (IllegalStateException e) {
            assertTrue(true);
            assertFalse(help_check(db, "3"));
        } catch (SQLException e) {
            assertTrue(true);
            assertFalse(help_check(db, "3"));
        } finally {
            db.safeClose();
        }
    }
