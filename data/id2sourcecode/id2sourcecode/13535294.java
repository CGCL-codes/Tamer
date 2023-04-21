    protected Selector getSharedSelector() throws IOException {
        if (SHARED && SHARED_SELECTOR == null) {
            synchronized (NioSelectorPool.class) {
                if (SHARED_SELECTOR == null) {
                    SHARED_SELECTOR = Selector.open();
                    log.info("Using a shared selector for servlet write/read");
                }
            }
        }
        return SHARED_SELECTOR;
    }
