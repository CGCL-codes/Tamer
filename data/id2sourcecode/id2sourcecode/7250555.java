    @Test(timeout = 2000)
    public void useBoundedHashSet() throws InterruptedException {
        final BoundedHashSet<String> bset = new BoundedHashSet<String>(10);
        System.out.println(bset.getSize());
        final CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {

            public void run() {
                Assert.assertEquals(10, bset.getSize());
            }
        });
        Thread write = new Thread() {

            public void run() {
                for (int i = 0; i < 20; i++) {
                    try {
                        bset.add("" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    return;
                } catch (java.util.concurrent.BrokenBarrierException ex) {
                    return;
                }
            }
        };
        Thread remove = new Thread() {

            public void run() {
                for (int i = 0; i < 10; i++) {
                    bset.remove("" + i);
                }
                try {
                    barrier.await();
                } catch (InterruptedException ex) {
                    return;
                } catch (BrokenBarrierException ex) {
                    return;
                }
            }
        };
        write.start();
        while (true) {
            if (bset.getSize() == 10) {
                remove.start();
                break;
            }
        }
        Assert.assertEquals(10, bset.getSize());
    }
