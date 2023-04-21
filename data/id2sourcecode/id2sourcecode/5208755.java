    @Test
    public void testConcurrentReadWrite_2() throws InterruptedException {
        final FlowWindow fw = new FlowWindow(2, 64);
        Thread reader = new Thread(new Runnable() {

            public void run() {
                doRead(fw);
            }
        });
        reader.setName("reader");
        Thread writer = new Thread(new Runnable() {

            public void run() {
                doWrite(fw);
            }
        });
        writer.setName("writer");
        writer.start();
        reader.start();
        int c = 0;
        while (read && write && c < 10) {
            Thread.sleep(1000);
            c++;
        }
        assertFalse("An error occured in reader or writer", fail);
    }
