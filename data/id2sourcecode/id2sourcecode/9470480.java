    public boolean download(String command, MyFile from, MyFile to) throws IOException {
        configurePROT(PROTdata);
        crc.reset();
        if (crcCache.containsKey(from.getName())) computeCRC = true; else computeCRC = false;
        SSLSocketChannel sc = null;
        boolean append = false;
        if (cfg.getPassive()) {
            sc = doPASV(command + from.getName());
        } else {
            sc = acceptConnection(command + from.getName());
        }
        Date d1 = new Date();
        if (sc == null) {
            return true;
        }
        if (PROTdata) sc.tryTLS(3);
        if (rest > 0) {
            append = true;
        }
        FileOutputStream fos = new FileOutputStream(panel.getOtherPanel().getDir() + File.separator + to.getName(), append);
        FileChannel fc = fos.getChannel();
        int amount;
        dbuf.clear();
        long size = rest;
        Date d3 = new Date();
        long diffSize = 0;
        long diffTime = 0;
        boolean ret = true;
        long start = System.currentTimeMillis();
        long elapsedStart = start;
        try {
            while ((amount = sc.read(dbuf)) != -1 && !aborted) {
                if (amount == 0) {
                    if ((System.currentTimeMillis() - start) > timeout) {
                        break;
                    }
                    try {
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                    }
                } else {
                    start = System.currentTimeMillis();
                }
                size += amount;
                diffSize += amount;
                if ((diffTime = System.currentTimeMillis() - d3.getTime()) > 1000) {
                    panel.getFrame().setStatusBar(from.getName(), from.getSize(), diffSize / (diffTime / 1000.0), size, (System.currentTimeMillis() - elapsedStart) / 1000);
                    String perf = Utilities.humanReadable(size) + "@" + Utilities.humanReadable(diffSize / (diffTime / 1000.0)) + "/s";
                    panel.getFrame().setTitle(perf);
                    diffSize = 0;
                    diffTime = 0;
                    d3 = new Date();
                }
                if (dbuf.remaining() > 0) {
                    continue;
                }
                dbuf.flip();
                fc.write(dbuf);
                crcUpdate();
                dbuf.clear();
            }
            dbuf.flip();
            fc.write(dbuf);
            crcUpdate();
            dbuf.clear();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            Utilities.saveStackTrace(e);
            ret = false;
        }
        if (aborted) {
            ret = false;
            aborted = false;
            for (int i = 0; i < 10 && gThread.ret == 0; i++) {
                try {
                    sleep(50);
                } catch (InterruptedException e) {
                }
            }
        }
        if (size < from.getSize()) {
            ret = false;
        }
        Date d2 = new Date();
        panel.getFrame().setStatusBar("", 0, 0.0, 0, 0);
        panel.getFrame().setTitle("wlFxp");
        fc.force(true);
        fos.close();
        sc.close();
        if (computeCRC) {
            panel.getFrame().getStatusArea().append(to.getName() + " crc32: " + hexformat(crc.getValue(), 8), panel.getColor());
            if (crcCache.get(from.getName()).equals(hexformat(crc.getValue(), 8))) panel.getFrame().getStatusArea().append("checksum correct", panel.getColor()); else panel.getFrame().getStatusArea().append("checksum incorrect", "red");
        }
        gThread.ret = 0;
        getTransferRate(d2.getTime() - d1.getTime(), size - rest);
        return ret;
    }
