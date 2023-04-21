    public ServerSessionImpl(ClassLoader cl, ExternalizableFactory ef, boolean smooth_close, int write_idle_time_sec, int read_idle_time_sec, int keepalive_interval_sec) {
        log = LoggerFactory.getLogger(getClass().getName());
        Codec = new NetPackageCodec(cl, ef);
        Connector = new NioSocketConnector();
        Connector.getSessionConfig().setReaderIdleTime(read_idle_time_sec);
        Connector.getSessionConfig().setWriterIdleTime(write_idle_time_sec);
        if (keepalive_interval_sec > 0) {
            keep_alive = new KeepAlive(Codec, keepalive_interval_sec, Math.min(write_idle_time_sec, read_idle_time_sec));
            Connector.getFilterChain().addLast("keep-alive", keep_alive);
        }
        Connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(Codec));
        Connector.setHandler(this);
        this.clean_task = new CleanTask();
        if (smooth_close) {
            Runtime.getRuntime().addShutdownHook(clean_task);
        }
    }
