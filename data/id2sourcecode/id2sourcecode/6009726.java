    public void run() {
        java.util.Date now = null;
        java.util.Date then = new java.util.Date();
        try {
            long allreads = 0;
            long allwrites = 0;
            double alldiff = 0;
            while (true) {
                long reads;
                long writes;
                synchronized (lt) {
                    lt.readcount = 0;
                    lt.writecount = 0;
                }
                Thread.sleep(10000);
                synchronized (lt) {
                    reads = lt.readcount;
                    writes = lt.writecount;
                }
                now = new java.util.Date();
                allreads += reads;
                allwrites += writes;
                double diff = (double) (now.getTime() - then.getTime());
                alldiff += diff;
                System.out.println("Res:" + (reads / diff) * 1000 + " reads/sec, " + (writes / diff) * 1000 + " writes/sec TOTALS:" + (allreads / alldiff) * 1000 + "r/s " + (allwrites / alldiff) * 1000 + "w/s");
                then = new java.util.Date();
            }
        } catch (Exception huh) {
            Code.fail(huh.toString());
        }
    }
