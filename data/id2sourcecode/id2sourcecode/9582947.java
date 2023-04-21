    protected void connectAndListener(final Process p) {
        ctrlSigRecvTime = System.currentTimeMillis();
        final InputStream ais = p.getInputStream();
        final OutputStream aos = p.getOutputStream();
        final InputStream aes = p.getErrorStream();
        final InputStream bis = System.in;
        final OutputStream bos = System.out;
        final OutputStream bes = System.err;
        Thread listener = new Thread() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(CtrlSigMaxWaitTime);
                        long diff = System.currentTimeMillis() - ctrlSigRecvTime;
                        if (diff > CtrlSigMaxWaitTime) {
                            p.destroy();
                            System.exit(1);
                        }
                        Thread.yield();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        listener.start();
        Thread ist = new Thread() {

            public void run() {
                while (true) {
                    try {
                        int read = bis.read();
                        if (read == -1) {
                            return;
                        }
                        if (read == CTRL_SIGNAL) {
                            ctrlSigRecvTime = System.currentTimeMillis();
                            continue;
                        }
                        aos.write(read);
                        aos.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        ist.start();
        Thread ost = new Thread() {

            public void run() {
                while (true) {
                    try {
                        int read = ais.read();
                        if (read == -1) {
                            return;
                        }
                        bos.write(read);
                        bos.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        ost.start();
        Thread est = new Thread() {

            public void run() {
                while (true) {
                    try {
                        int read = aes.read();
                        if (read == -1) {
                            return;
                        }
                        bes.write(read);
                        bes.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        est.start();
    }
