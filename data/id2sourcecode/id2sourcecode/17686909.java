    public void run(int count) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(count);
        final AtomicInteger errors = new AtomicInteger(0);
        final AtomicInteger oks = new AtomicInteger(0);
        Runnable r = new Runnable() {

            public void run() {
                int read = 0;
                int totalRead = 0;
                HttpURLConnection conn = null;
                InputStream in = null;
                OutputStream out = null;
                try {
                    URL url = new URL(surl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    out = conn.getOutputStream();
                    out.write("some text".getBytes());
                    in = conn.getInputStream();
                    byte[] buffer = new byte[4096];
                    while ((read = in.read(buffer)) > 0) {
                        totalRead += read;
                    }
                    if (totalRead != conn.getContentLength()) {
                        System.out.println("KO " + Thread.currentThread().getName() + " " + conn.getContentLength() + " " + totalRead + " " + read);
                        errors.incrementAndGet();
                    } else {
                        oks.incrementAndGet();
                    }
                } catch (Exception ex) {
                    errors.incrementAndGet();
                    System.out.println(">>> " + Thread.currentThread().getName() + " " + ex.getMessage());
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    conn.disconnect();
                    latch.countDown();
                    long c = latch.getCount();
                    if (c % 50 == 0) {
                        System.out.println(c);
                    } else {
                        System.out.print('.');
                    }
                }
            }
        };
        for (int i = 0; i < count; ++i) {
            executorService.execute(r);
        }
        latch.await();
        System.out.println("OKS: " + oks.intValue() + " ERRORS: " + errors.intValue());
    }
