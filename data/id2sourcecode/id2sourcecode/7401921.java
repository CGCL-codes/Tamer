    public RollingFileWriter(final Format format, final Properties properties) throws IOException {
        super(format);
        long maxSize;
        try {
            maxSize = Long.valueOf(properties.getProperty("maxsize"));
        } catch (Exception e) {
            maxSize = Long.MAX_VALUE;
        }
        this.maxSize = maxSize;
        Period period;
        try {
            period = PERIOD_FORMAT.parsePeriod(properties.getProperty("period"));
        } catch (Exception e) {
            period = null;
        }
        this.period = period;
        if (period != null) {
            DateTime rollDateTime;
            try {
                rollDateTime = DATE_FORMAT.parseDateTime(properties.getProperty("rolltime"));
            } catch (Exception e) {
                rollDateTime = new DateTime();
            }
            final DateTime now = new DateTime();
            while (now.compareTo(rollDateTime) >= 0) {
                rollDateTime = rollDateTime.withPeriodAdded(period, 1);
            }
            rollTime = rollDateTime.getMillis();
        }
        final boolean append = Boolean.valueOf(properties.getProperty("append"));
        fileName = properties.getProperty("filename");
        if (fileName == null) {
            throw new NullPointerException("The filename property has not been set");
        }
        file = new File(fileName);
        if (append) {
            size = file.length();
        }
        FileOutputStream outputStream = new FileOutputStream(file, append);
        fileLock = outputStream.getChannel().tryLock();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                synchronized (lock) {
                    FileLock fileLock = getFileLock();
                    try {
                        if (fileLock != null) {
                            fileLock.release();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
        out = new PrintStream(outputStream);
    }
