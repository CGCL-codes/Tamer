    public void run() {
        if (out == null) return;
        Thread.currentThread().setName("LoggerThread");
        write(Logger.formatMsg(Logger.INFO, "Starting Logger Thread..." + newLine));
        loggerThreadReady = true;
        try {
            while (true) {
                write(queue.take());
                msgCount++;
                int depth = queue.size();
                if (depth > maxDepth) maxDepth = depth;
            }
        } catch (InterruptedException e) {
            try {
                ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
                long cpu = threadBean.getThreadCpuTime(this.getId());
                long cpums = cpu / NANOS_PER_MILLI;
                if (cpums < 1) cpums = 1;
                write(Logger.formatMsg(Logger.INFO, "Logger Thread Ending, " + msgCount + " Messages Processed, Max Queued Messages was " + maxDepth + " CPU Used: " + cpums + "ms" + newLine));
                write(RUN_DELIM + newLine);
                out.close();
            } catch (IOException e1) {
            }
        }
    }
