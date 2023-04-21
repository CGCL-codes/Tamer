    protected void startKeepAliveTimer() {
        keep_alive_ticks = MIN_KEEPALIVE_TICKS + random.nextInt(MAX_KEEPALIVE_TICKS - MIN_KEEPALIVE_TICKS);
    }
