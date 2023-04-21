        BigTable(String tablename) {
            this.tablename = tablename;
            Request.execute("create " + tablename + " (a,b,c,d,e,f,g) key(a) index(b,c)");
            for (int i = 0; i < N / 100; ++i) {
                Transaction t = theDB.readwriteTran();
                for (int j = 0; j < 100; ++j) t.addRecord(tablename, record());
                t.ck_complete();
            }
        }
