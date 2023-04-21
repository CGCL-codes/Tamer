    private static synchronized void checkTextTraceFile() {
        long time = System.currentTimeMillis();
        if ((time - textTosCreationTime) > ROLLOVER_TIME) {
            textTraceInitialiazed = false;
            if (textTos != null) {
                try {
                    textTos.close();
                } catch (IOException e) {
                }
                textTos = null;
            }
        }
        if (!textTraceInitialiazed) {
            textTraceInitialiazed = true;
            Calendar d = new GregorianCalendar();
            long year = d.get(Calendar.YEAR);
            long month = d.get(Calendar.MONTH) + 1;
            long day = d.get(Calendar.DAY_OF_MONTH);
            if (requestTraceFile == null) {
                return;
            }
            String currentTextFile = requestTraceFile + "." + year + "." + month + "." + day;
            try {
                textTos = new FileOutputStream(currentTextFile + ".txt", true).getChannel();
                textTosCreationTime = time;
                write(textTos, "\n");
            } catch (IOException e) {
                ZooLog.logException(e);
                return;
            }
            ZooLog.logWarn("*********** Traced requests text saved to " + currentTextFile + ".txt");
        }
    }
