        private void update() {
            nupdates.incrementAndGet();
            int n = random(N);
            Transaction t = theDB.readwriteTran();
            try {
                Query q = CompileQuery.parse(t, serverData, "update " + tablename + " where b = " + n + " set c = " + random(N));
                ((QueryAction) q).execute();
                t.ck_complete();
            } catch (SuException e) {
                nupdatesfailed.incrementAndGet();
                t.abort();
                throwUnexpected(e);
            }
        }
