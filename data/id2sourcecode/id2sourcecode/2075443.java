    @Test
    public void update_visibility() {
        makeTable(5);
        Transaction t = db.readwriteTran();
        t.updateRecord("test", "a", key(1), record(9));
        check(0, 1, 2, 3, 4);
        t.ck_complete();
        check(0, 2, 3, 4, 9);
        db.checkTransEmpty();
    }
