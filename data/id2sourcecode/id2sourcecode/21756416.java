    public NonThreadedEchoLineHandler(int delay, boolean syncFlush, boolean writeTofile) throws IOException {
        this.delay = delay;
        this.syncFlush = syncFlush;
        if (writeTofile) {
            File tempFile = File.createTempFile("test", "test");
            tempFile.deleteOnExit();
            fc = new RandomAccessFile(File.createTempFile("test", "test"), "rw").getChannel();
            System.out.println("write received data into " + tempFile.getAbsolutePath());
        }
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                try {
                    String rate = printRate();
                    if (fc != null) {
                        System.out.println(rate + "(file size=" + fc.position() + ")");
                    } else {
                        System.out.println(rate);
                    }
                } catch (Exception ignore) {
                }
            }
        };
        timer.schedule(task, PRINT_PERIOD, PRINT_PERIOD);
        System.out.println("Echo handler initialized. Printing each rate (req/sec) each " + DataConverter.toFormatedDuration(PRINT_PERIOD));
    }
