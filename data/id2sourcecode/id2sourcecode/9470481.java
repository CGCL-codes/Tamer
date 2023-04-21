    public boolean upload(String command, MyFile from, MyFile to) throws IOException {
        SSLSocketChannel sc = null;
        configurePROT(PROTdata);
        if (cfg.getPassive()) {
            sc = doPASV(command + to.getName());
        } else {
            sc = acceptConnection(command + to.getName());
        }
        Date d1 = new Date();
        if (sc == null) {
            return true;
        }
        if (PROTdata) sc.tryTLS(3);
        FileInputStream fis = new FileInputStream(new File(panel.getOtherPanel().getDir() + File.separator + from.getName()));
        FileChannel fc = fis.getChannel();
        if (rest > 0) {
            fc.position(rest);
        }
        int amount;
        ubuf.clear();
        long size = rest;
        Date d3 = new Date();
        long diffSize = 0;
        long diffTime = 0;
        boolean ret = true;
        int s_amount = 0;
        long start = System.currentTimeMillis();
        long elapsedStart = start;
        int i;
        try {
            bigwhile: while ((amount = fc.read(ubuf)) != -1 && !aborted) {
                ubuf.flip();
                start = System.currentTimeMillis();
                i = 0;
                while ((i = sc.write(ubuf)) != -1 && !aborted) {
                    s_amount += i;
                    size += i;
                    diffSize += i;
                    if ((diffTime = System.currentTimeMillis() - d3.getTime()) > 1000) {
                        panel.getFrame().setStatusBar(from.getName(), from.getSize(), diffSize / (diffTime / 1000.0), size, (System.currentTimeMillis() - elapsedStart) / 1000);
                        String perf = Utilities.humanReadable(size) + "@" + Utilities.humanReadable(diffSize / (diffTime / 1000.0)) + "/s";
                        panel.getFrame().setTitle(perf);
                        diffSize = 0;
                        diffTime = 0;
                        d3 = new Date();
                    }
                    if (amount <= s_amount) {
                        break;
                    }
                    if (i == 0) {
                        if ((System.currentTimeMillis() - start) > timeout) {
                            break bigwhile;
                        } else {
                            try {
                                Thread.sleep(4);
                            } catch (InterruptedException e) {
                            }
                        }
                    } else {
                        start = System.currentTimeMillis();
                    }
                }
                if (i == -1) {
                    break;
                }
                s_amount = 0;
                ubuf.clear();
            }
        } catch (IOException e) {
            Utilities.saveStackTrace(e);
            ret = false;
        }
        if (aborted) {
            ret = false;
            for (int j = 0; j < 10 && gThread.ret == 0; j++) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                }
            }
        }
        if (size < from.getSize()) {
            ret = false;
        }
        panel.getFrame().setStatusBar("", 0, 0.0, 0, 0);
        panel.getFrame().setTitle("wlFxp");
        fis.close();
        sc.close();
        while (!aborted && gThread.ret == 0) {
            try {
                sleep(50);
            } catch (InterruptedException e) {
            }
        }
        gThread.ret = 0;
        aborted = false;
        Date d2 = new Date();
        getTransferRate(d2.getTime() - d1.getTime(), size - rest);
        return ret;
    }
