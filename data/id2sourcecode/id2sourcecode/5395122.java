    public void doTestMega() {
        try {
            final BlockingRAF raf = new BlockingRAF(new TempRaf());
            int writerCount = 3;
            int readerCount = 3;
            Thread[] writers = new Thread[writerCount];
            for (int i = 0; i < writerCount; i++) {
                writers[i] = new Thread() {

                    public void run() {
                        RangeSet rs = new RangeSet();
                        try {
                            int min, max;
                            while (rs.size() != b.length) {
                                if (rs.isEmpty()) {
                                    min = 0;
                                } else if (rand.nextInt(10) == 0) {
                                    min = (int) ((Range) rs.iterator().next()).getMax() + 1;
                                } else {
                                    min = (int) ((Range) rs.iterator().next()).getMax() + rand.nextInt(b.length - (int) rs.size());
                                }
                                max = rand.nextInt(b.length - min) + min;
                                rs.add(min, max);
                                raf.seekAndWrite(min, b, min, max - min + 1);
                            }
                        } catch (IOException e) {
                            e.printStackTrace(System.out);
                            fail("" + e);
                        }
                    }
                };
                writers[i].start();
            }
            final Thread[] readers = new Thread[readerCount];
            for (int i = 0; i < readerCount; i++) {
                readers[i] = new Thread() {

                    public void run() {
                        RAFInputStream ris = new RAFInputStream(raf);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] b2 = new byte[8192 * (rand.nextInt(5) + 1)];
                        int c;
                        try {
                            while ((c = ris.read(b2)) != -1) {
                                baos.write(b2, 0, c);
                            }
                            baos.close();
                            assert (Util.arraysEqual(baos.toByteArray(), 0, b, 0, b.length));
                        } catch (IOException e) {
                            fail("" + e);
                        }
                    }
                };
                readers[i].start();
            }
            for (int i = 0; i < writerCount; i++) {
                try {
                    writers[i].join();
                } catch (InterruptedException e) {
                    fail("" + e);
                }
            }
            raf.setReadOnly();
            for (int i = 0; i < readerCount; i++) {
                try {
                    readers[i].join();
                } catch (InterruptedException e) {
                    fail("" + e);
                }
            }
            raf.close();
        } catch (IOException e) {
            fail("" + e);
        }
    }
