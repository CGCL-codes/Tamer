        private void append() {
            nappends.incrementAndGet();
            Transaction t = theDB.readwriteTran();
            try {
                t.addRecord(tablename, record());
            } catch (SuException e) {
                throwUnexpected(e);
            } finally {
                if (t.complete() != null) nappendsfailed.incrementAndGet();
            }
        }
