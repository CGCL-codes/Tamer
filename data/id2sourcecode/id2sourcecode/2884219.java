    public static XLog createLog(int minTraces, int maxTraces, int minTraceLength, int maxTraceLength) {
        XLog log = factory.createLog();
        addAttributes(log, 0.9, 3, 50);
        int length = minTraces + random.nextInt(maxTraces - minTraces);
        for (int i = 0; i < length; i++) {
            log.add(createTrace(minTraceLength, maxTraceLength));
        }
        return log;
    }
