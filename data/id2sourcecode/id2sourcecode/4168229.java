    @Test
    public void testTransaction() {
        assertFalse(dao.isTransactionStarted());
        dao.startTransaction();
        assertTrue(dao.isTransactionStarted());
        try {
            dao.executeUpdate("insert into test (id, name) values (1,'test')");
            dao.executeUpdate("update test set name='test2' where id='1'");
            dao.executeUpdate("delete from test where id='1'");
            dao.commit();
            assertTrue(dao.isTransactionStarted());
        } catch (Exception e) {
            dao.rollback();
            dao.handleException(e);
        } finally {
            dao.endTransaction();
            assertFalse(dao.isTransactionStarted());
        }
    }
