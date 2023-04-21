    private void testSimpleExec(JCARunnerConnection con, final PrintWriter writer) throws Exception {
        final Semaphore s = new Semaphore(0);
        Runnable r = new Runnable() {

            public void run() {
                try {
                    Thread.sleep(rand.nextInt(100));
                } catch (InterruptedException e) {
                    e.printStackTrace(writer);
                    writer.flush();
                }
                s.release();
            }
        };
        writer.println("Before starting vats: " + Thread.currentThread().getName());
        writer.flush();
        VatRunner runner = con.getRunner();
        new Vat(runner, "vat 1").enqueue(r);
        new Vat(runner, "vat 2").enqueue(r);
        writer.println("Vats started");
        writer.flush();
        try {
            s.acquire(2);
            if (s.availablePermits() != 0) {
                writer.println("Something went wrong. " + "The Semaphore contains more permits than expected.");
                writer.flush();
            }
        } catch (InterruptedException e) {
            e.printStackTrace(writer);
            writer.flush();
        }
    }
