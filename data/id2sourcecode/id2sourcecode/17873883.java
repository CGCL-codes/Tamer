    public boolean processMessage(String from, InputStream fwdMsg) {
        Date now = new Date();
        final String curDateTime = LONG_FORMAT.format(now);
        final String curDateTime2 = SHORT_FORMAT.format(now);
        String hostAddress;
        try {
            hostAddress = InetAddress.getByName(fromSrv).getHostAddress();
        } catch (Exception e) {
            hostAddress = "unknown";
        }
        final String recFrom = "Received: from " + fromSrv + " ([" + hostAddress + "]) by localhost with webDAV (fetchExc) for mbox (single-drop); " + curDateTime;
        BufferedOutputStream bos = null;
        FileLock lock;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(mboxFile, true);
        } catch (IOException ioe) {
            System.err.println("Can't open file:" + mboxFile);
            return false;
        }
        try {
            lock = lockFile(fos.getChannel(), false);
        } catch (IOException ioe) {
            System.out.println("error in locking file:" + mboxFile);
            ioe.printStackTrace();
            return false;
        }
        bos = new BufferedOutputStream(fos);
        try {
            int data;
            int fromCnt = 0;
            boolean fromOn = false;
            boolean cr = false, lf = false;
            String fromQuote = "From ";
            int fromLen = fromQuote.length();
            String fromLine = "From " + from + "  " + curDateTime2;
            bos.write(fromLine.getBytes(), 0, fromLine.length());
            bos.write('\n');
            String returnPath = "Return-Path: <" + from + ">";
            bos.write(returnPath.getBytes(), 0, returnPath.length());
            bos.write('\n');
            bos.write(recFrom.getBytes(), 0, recFrom.length());
            bos.write('\n');
            while (true) {
                data = fwdMsg.read();
                if (data < 0) break;
                if (data == '\r' && fromOn == false) {
                    cr = true;
                    lf = false;
                    continue;
                }
                if (data == '\n' && fromOn == false) {
                    lf = true;
                    bos.write(data);
                    continue;
                }
                if (cr == true && lf == true && data == fromQuote.charAt(0)) {
                    cr = false;
                    lf = false;
                    fromOn = true;
                    fromCnt = 1;
                    continue;
                }
                if (fromOn == true && data == fromQuote.charAt(fromCnt)) {
                    if (fromCnt < fromLen - 1) {
                        fromCnt++;
                        continue;
                    } else {
                        bos.write('>');
                        bos.write(fromQuote.getBytes(), 0, fromLen);
                        fromOn = false;
                        continue;
                    }
                }
                if (fromOn == true) {
                    bos.write(fromQuote.getBytes(), 0, fromCnt);
                    fromOn = false;
                }
                cr = false;
                lf = false;
                if (data != '\r') bos.write(data);
            }
            bos.write('\n');
            bos.flush();
            lock.release();
            bos.close();
        } catch (IOException e) {
            System.out.println("Error in writing to mbox: " + e);
            e.printStackTrace();
            return false;
        } finally {
            try {
                lock.release();
            } catch (IOException ioe) {
            }
        }
        return true;
    }
