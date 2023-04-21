    @Test
    public void update_conflict() {
        makeTable(3);
        Transaction t1 = db.readwriteTran();
        t1.updateRecord("test", "a", key(1), record(5));
        Transaction t2 = db.readwriteTran();
        try {
            t2.updateRecord("test", "a", key(1), record(6));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        assert (t2.isAborted());
        assertNotNull(t2.complete());
        assertTrue(t2.conflict().contains("write-write conflict"));
        Transaction t3 = db.readwriteTran();
        try {
            t3.updateRecord("test", "a", key(1), record(6));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        assert (t3.isAborted());
        assertNotNull(t3.complete());
        assertTrue(t3.conflict().contains("write-write conflict"));
        t1.ck_complete();
        db.checkTransEmpty();
    }
