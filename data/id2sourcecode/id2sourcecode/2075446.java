    @Test
    public void phantoms() {
        makeTable(1000);
        Transaction t1 = db.readwriteTran();
        getLast("test", t1);
        t1.updateRecord("test", "a", key(1), record(1));
        Transaction t2 = db.readwriteTran();
        try {
            t2.addRecord("test", record(1000));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        assertTrue(t2.isAborted());
        Transaction t3 = db.readwriteTran();
        try {
            t3.removeRecord("test", "a", key(999));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        assertTrue(t3.isAborted());
        t1.ck_complete();
        db.checkTransEmpty();
    }
