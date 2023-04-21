    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("Usage: " + IntensiveMappingTest.class.getName() + " multi|single blockSize numberOfMappings file1 [file2...]");
            System.out.println("These files will not be modified");
            return;
        }
        boolean multithreading;
        if (args[0].equalsIgnoreCase("multi")) multithreading = true; else if (args[0].equalsIgnoreCase("single")) multithreading = false; else throw new IllegalArgumentException("First argument must be \"multi\" or \"single\"");
        final int blockSize = Integer.parseInt(args[1]);
        if (blockSize <= 0) throw new IllegalArgumentException("blockSize <= 0");
        final int numberOfMappings = Integer.parseInt(args[2]);
        if (numberOfMappings <= 0) throw new IllegalArgumentException("numberOfMappings <= 0");
        RandomAccessFile[] raf = new RandomAccessFile[args.length - 3];
        final FileChannel[] fc = new FileChannel[raf.length];
        long minLen = Long.MAX_VALUE;
        for (int k = 0; k < raf.length; k++) {
            raf[k] = new RandomAccessFile(args[k + 3], "r");
            fc[k] = raf[k].getChannel();
            minLen = Math.min(minLen, raf[k].length());
        }
        final long len = minLen;
        final long t1 = System.nanoTime();
        if (multithreading) {
            final Thread[] threads = new Thread[numberOfMappings];
            final AtomicInteger numberOfSimultaneousMappings = new AtomicInteger(0);
            for (int threadIndex = 0; threadIndex < numberOfMappings; threadIndex++) {
                final int customer = threadIndex;
                threads[customer] = new Thread() {

                    public void run() {
                        for (long k = 0, n = len / blockSize; k < n; k++) {
                            int nsm = -1;
                            try {
                                MappedByteBuffer mbb = fc[customer % fc.length].map(FileChannel.MapMode.READ_ONLY, k * blockSize, blockSize);
                                nsm = numberOfSimultaneousMappings.incrementAndGet();
                                synchronized (fc) {
                                    mbb.load();
                                }
                                ByteBuffer bb = ByteBuffer.allocate(1000000);
                                for (int j = 0, m = Math.min(mbb.limit(), bb.limit()); j < m; j++) {
                                    bb.put(j, mbb.get(j));
                                }
                                numberOfSimultaneousMappings.decrementAndGet();
                            } catch (IOException e) {
                                synchronized (fc) {
                                    System.out.printf("%n%nERROR in thread %d!%n", customer);
                                    e.printStackTrace();
                                    System.gc();
                                    try {
                                        System.out.println("Press ENTER to continue...");
                                        new BufferedReader(new InputStreamReader(System.in)).readLine();
                                    } catch (IOException e1) {
                                    }
                                }
                            }
                            long t2 = System.nanoTime();
                            synchronized (fc) {
                                System.out.printf("%2d, %2d simultaneous: %.3f MB read in %.5f seconds (%.3f MB/sec)%n", customer, nsm, (k + 1) * blockSize / 1048576.0, (t2 - t1) * 1e-9, (k + 1) * blockSize / 1048576.0 / ((t2 - t1) * 1e-9));
                            }
                        }
                    }
                };
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            final MappedByteBuffer[] mbb = new MappedByteBuffer[numberOfMappings];
            for (long k = 0, n = len / blockSize; k < n; k++) {
                for (int mi = 0; mi < numberOfMappings; mi++) {
                    try {
                        mbb[mi] = fc[mi % fc.length].map(FileChannel.MapMode.READ_ONLY, k * blockSize, blockSize);
                        mbb[mi].load();
                    } catch (IOException e) {
                        System.out.printf("%n%nERROR while mapping #%d!%n", mi);
                        e.printStackTrace();
                        try {
                            System.out.println("Press ENTER to continue...");
                            new BufferedReader(new InputStreamReader(System.in)).readLine();
                        } catch (IOException e1) {
                        }
                    }
                    long t2 = System.nanoTime();
                    System.out.printf("%3d: %.3f MB read in %.5f seconds (%.3f MB/sec)%n", mi, (k + 1) * blockSize / 1048576.0, (t2 - t1) * 1e-9, (k + 1) * blockSize / 1048576.0 / ((t2 - t1) * 1e-9));
                }
            }
        }
        System.out.println("OK");
    }
