    @Test
    public void add_conflict() {
        makeTable();
        Transaction t1 = db.readwriteTran();
        t1.addRecord("test", record(99));
        Transaction t2 = db.readwriteTran();
        try {
            t2.addRecord("test", record(99));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        t1.ck_complete();
        assertNotNull(t2.complete());
        assertTrue(t2.conflict().contains("write-write conflict"));
        db.checkTransEmpty();
    }
