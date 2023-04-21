    public void test() {
        try {
            System.out.println("Start space");
            theSpace = new SpaceImpl(null);
            System.out.println("Prepare entry");
            EntryMangler myMangler = new EntryMangler();
            MangledEntry myPackedEntry = myMangler.mangle(new TestEntry().init());
            System.out.println("Start taker");
            Thread myThread = new Thread(this);
            myThread.start();
            System.out.println("Start taker");
            myThread = new Thread(this);
            myThread.start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException anIE) {
            }
            System.out.println("Do write: " + theSpace.write(myPackedEntry, null, Lease.FOREVER) + Thread.currentThread());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException anIE) {
            }
            System.out.println("Do write: " + theSpace.write(myPackedEntry, null, Lease.FOREVER) + Thread.currentThread());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException anIE) {
            }
            theSpace.stop();
        } catch (Exception anE) {
            System.err.println("Writer failed :(");
            anE.printStackTrace(System.err);
        }
    }
