    protected int req(String s) {
        Transaction tran = db.readwriteTran();
        try {
            Query q = CompileQuery.parse(tran, serverData, s);
            int n = ((QueryAction) q).execute();
            tran.ck_complete();
            return n;
        } finally {
            tran.abortIfNotComplete();
        }
    }
