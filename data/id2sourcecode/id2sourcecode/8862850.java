        @Override
        public void run() {
            Transaction t = theDB.readwriteTran();
            Query q = CompileQuery.query(t, serverData, tablename);
            try {
                Row r = q.get(Dir.NEXT);
                Record rec = r.getFirstData();
                t.updateRecord(rec.off(), rec);
            } catch (SuException e) {
                throwUnexpected(e);
            }
            if (t.complete() != null) nfailed.incrementAndGet();
            nreps.incrementAndGet();
        }
