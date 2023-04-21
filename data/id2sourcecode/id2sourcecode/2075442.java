    @Test
    public void update_conflict_2() {
        makeTable(3);
        Transaction t1 = db.readwriteTran();
        Transaction t2 = db.readwriteTran();
        t2.updateRecord("test", "a", key(1), record(6));
        t2.ck_complete();
        try {
            t1.updateRecord("test", "a", key(1), record(6));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        assert (t1.isAborted());
        assertNotNull(t1.complete());
        assertTrue(t1.conflict().contains("write-write conflict"));
        db.checkTransEmpty();
    }
