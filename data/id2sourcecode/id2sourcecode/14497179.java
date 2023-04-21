    public void run() {
        String tofType;
        if (flagNew) tofType = "TOF Download"; else tofType = "TOF Download Back";
        int waitTime = waitTime = (int) (Math.random() * 5000);
        mixed.wait(waitTime);
        System.out.println(tofType + " Thread started for board " + board);
        frame1.tofDownloadThreads++;
        frame1.activeTofThreads.add(board);
        UpdateIdThread uit = null;
        if (flagNew && !mixed.isElementOf(board, block)) {
            uit = new UpdateIdThread(board);
            uit.start();
            mixed.wait(5000);
        }
        String val = new StringBuffer().append(frame1.keypool).append(board).append(".key").toString();
        String state = SettingsFun.getValue(val, "state");
        if (state.equals("writeAccess") || state.equals("readAccess")) {
            publicKey = SettingsFun.getValue(val, "publicKey");
            secure = true;
        } else {
            secure = false;
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        if (this.flagNew) {
            downloadDate(cal);
            if (uit != null) {
                try {
                    uit.join();
                } catch (InterruptedException ex) {
                }
            }
        } else {
            GregorianCalendar firstDate = new GregorianCalendar();
            firstDate.setTimeZone(TimeZone.getTimeZone("GMT"));
            firstDate.set(Calendar.YEAR, 2001);
            firstDate.set(Calendar.MONTH, 5);
            firstDate.set(Calendar.DATE, 11);
            int counter = 0;
            while (cal.after(firstDate) && counter < maxMessageDownload) {
                counter++;
                cal.add(Calendar.DATE, -1);
                downloadDate(cal);
            }
        }
        System.out.println(tofType + " Thread stopped for board " + board);
        frame1.activeTofThreads.removeElement(board);
        frame1.tofDownloadThreads--;
        synchronized (frame1.TOFThreads) {
            frame1.TOFThreads.removeElement(this);
        }
    }
