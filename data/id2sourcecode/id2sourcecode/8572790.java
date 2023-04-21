    @Test
    public void testReadQueue() throws Exception {
        final BlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();
        final SerialPort port = open();
        try {
            final OutputStream out = port.getOutputStream();
            port.addEventListener(new SerialPortEventListener() {

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                        try {
                            InputStream in = port.getInputStream();
                            int size = in.available();
                            byte[] buf = new byte[size];
                            in.read(buf);
                            queue.put(buf);
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new AssertionError(e);
                        }
                    }
                }
            });
            final long BEGIN[] = new long[1];
            final boolean[] flg = { true };
            Runnable writer = new Runnable() {

                @Override
                public void run() {
                    try {
                        int wait = 50;
                        System.out.println("BEGIN ...");
                        out.write("Q*".getBytes());
                        out.flush();
                        Thread.sleep(wait);
                        assertEquals("Q*", read(port));
                        out.write("?*".getBytes());
                        out.flush();
                        Thread.sleep(wait);
                        assertEquals("?1.1.0b00*", read(port));
                        out.write("V0*".getBytes());
                        out.flush();
                        Thread.sleep(wait);
                        assertEquals("V0*", read(port));
                        out.write("KONFIGURATION_1*".getBytes());
                        out.flush();
                        Thread.sleep(wait);
                        assertEquals("KONFIGURATION_1*", read(port));
                        System.out.println("END ...");
                        System.out.println("BEGIN ....");
                        port.notifyOnDataAvailable(true);
                        BEGIN[0] = System.currentTimeMillis();
                        out.write("H0*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("L0*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("R*".getBytes());
                        out.flush();
                        out.write("AFFFFFFFF*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("a000*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("I*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("S0*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("M0*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("G31*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("r*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("E*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("i*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("E*".getBytes());
                        out.flush();
                        dummyInput(out);
                        out.write("Q*".getBytes());
                        out.flush();
                        Thread.sleep(100);
                        out.write("KONFIGURATION_2*".getBytes());
                        out.flush();
                        Thread.sleep(100);
                        out.write("i*".getBytes());
                        out.flush();
                        out.write("E*".getBytes());
                        out.flush();
                        Thread.sleep(1000);
                        out.write("Q*".getBytes());
                        out.flush();
                        Thread.sleep(100);
                        out.write("KONFIGURATION_7*".getBytes());
                        out.flush();
                        Thread.sleep(100);
                        out.write("a000000000*".getBytes());
                        out.flush();
                        out.write("a100000001*".getBytes());
                        out.flush();
                        out.write("a200000002*".getBytes());
                        out.flush();
                        Thread.sleep(1000);
                        out.write("Q*".getBytes());
                        out.flush();
                        Thread.sleep(100);
                        out.write("KONFIGURATION_8*".getBytes());
                        out.flush();
                        Thread.sleep(100);
                        out.write("Tx*".getBytes());
                        out.flush();
                        dummyInput(out);
                        Thread.sleep(1000);
                        flg[0] = false;
                        System.out.println("END ....");
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

                private void dummyInput(final OutputStream out) throws IOException {
                    for (int i = 0; i < 5; i++) {
                        out.write("D0000*".getBytes());
                        out.flush();
                    }
                }
            };
            Runnable reader = new Runnable() {

                StringBuilder stb = new StringBuilder();

                @Override
                public void run() {
                    try {
                        while (flg[0]) {
                            byte[] data = queue.take();
                            stb.append(new String(data));
                            for (int i = stb.indexOf("*"); -1 < i; ) {
                                processCmd(stb.substring(0, i + 1));
                                stb.delete(0, i + 1);
                                i = stb.indexOf("*");
                            }
                        }
                    } catch (InterruptedException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                protected void processCmd(String cmd) {
                    System.out.println(cmd);
                    assertEquals(cmd.charAt(cmd.length() - 1), '*');
                    assertTrue(cmd.charAt(0) != '!');
                }
            };
            Thread wt = new Thread(writer);
            wt.setName("testReadQueue#WriterThread");
            final Thread rt = new Thread(reader);
            rt.setName("testReadQueue#ReaderThread");
            rt.start();
            wt.start();
            Runnable interruptor = new Runnable() {

                @Override
                public void run() {
                    while (BEGIN[0] < 1L || (System.currentTimeMillis() - BEGIN[0]) < 8000) {
                    }
                    rt.interrupt();
                }
            };
            Thread it = new Thread(interruptor);
            it.setName("testReadQueue#InterruptorThread");
            it.start();
            wt.join();
            rt.join();
            it.join();
            port.notifyOnDataAvailable(false);
            port.removeEventListener();
        } finally {
            CommPortUtil.close(port);
        }
    }
