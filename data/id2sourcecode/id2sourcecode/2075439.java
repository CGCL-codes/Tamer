    @Test
    public void delete_conflict() {
        makeTable(1000);
        Transaction t1 = db.readwriteTran();
        t1.removeRecord("test", "a", key(1));
        Transaction t2 = db.readwriteTran();
        t2.removeRecord("test", "a", key(999));
        t2.ck_complete();
        t1.ck_complete();
        db.checkTransEmpty();
        Transaction t3 = db.readwriteTran();
        t3.removeRecord("test", "a", key(4));
        Transaction t4 = db.readwriteTran();
        try {
            t4.removeRecord("test", "a", key(5));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        assertTrue(t4.isAborted());
        t3.ck_complete();
        assertNotNull(t4.complete());
        assertTrue(t4.conflict().contains("write-write conflict"));
        db.checkTransEmpty();
    }
