    @Test
    public void visibility() {
        db.checkTransEmpty();
        makeTable(1000);
        before = new int[] { 0, 1, 2, 997, 998, 999 };
        after = new int[] { 0, 2, 3, 998, 999, 9999 };
        Transaction t1 = add_remove();
        Transaction t2 = db.readonlyTran();
        checkBefore(t2);
        Transaction t3 = db.readwriteTran();
        checkBefore(t3);
        t1.ck_complete();
        Transaction t4 = db.readonlyTran();
        checkAfter(t4);
        t4.ck_complete();
        Transaction t5 = db.readwriteTran();
        checkAfter(t5);
        t5.ck_complete();
        checkBefore(t2);
        t2.ck_complete();
        checkBefore(t3);
        t3.ck_complete();
        db.checkTransEmpty();
    }
