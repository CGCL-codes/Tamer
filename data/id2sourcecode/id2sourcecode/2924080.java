    public void startupInit(Object arg) throws Exception {
        ht = (Hashtable) arg;
        acct = ht.get("Account").toString();
        reads = ((Number) ht.get("reads")).intValue();
        writes = ((Number) ht.get("writes")).intValue();
        if (writes > reads) Code.fail("wrtes>reads: Each write needs a read!");
        int threads = ((Number) ht.get("threads")).intValue();
        for (int i = 0; i < threads; i++) {
            Thread thr = new Thread(this, "LoadTest" + i);
            thr.start();
        }
        measurer meas = new measurer(this);
        Thread thr = new Thread(meas, "Measurer");
        meas.mine = thr;
        thr.start();
        thr.join();
    }
