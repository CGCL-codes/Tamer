    @Override
    public void init() {
        if (!checkInnerElement()) {
            throw new java.lang.RuntimeException("ConsumerSchedule start Error!,please check innerElement");
        }
        bundle = new RecordBundle<T>(config.getFlushInterval());
        writeBundleService = Executors.newFixedThreadPool(config.getWriterMaxCount());
        activeFlag = true;
    }
