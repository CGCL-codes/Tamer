    private void roll() {
        try {
            final StringBuilder rollFileName = new StringBuilder(fileName);
            rollFileName.append('.');
            rollFileName.append(System.currentTimeMillis());
            if (fileLock != null) {
                fileLock.release();
            }
            out.close();
            file.renameTo(new File(rollFileName.toString()));
            size = 0;
            file = new File(fileName);
            final FileOutputStream outputStream = new FileOutputStream(file);
            fileLock = outputStream.getChannel().tryLock();
            out = new PrintStream(outputStream);
            if (rollTime != Long.MAX_VALUE && period != null) {
                final DateTime now = new DateTime();
                DateTime rollDateTime = new DateTime(rollTime);
                try {
                    while (now.compareTo(rollDateTime) >= 0) {
                        rollDateTime = rollDateTime.withPeriodAdded(period, 1);
                    }
                    rollTime = rollDateTime.getMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                    rollTime = Long.MAX_VALUE;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
