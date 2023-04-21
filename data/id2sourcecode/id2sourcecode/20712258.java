    @Override
    public boolean appConnect(IConnection conn, Object[] params) {
        measureBandwidth(conn);
        if (conn instanceof IStreamCapableConnection) {
            IStreamCapableConnection streamConn = (IStreamCapableConnection) conn;
            SimpleConnectionBWConfig bwConfig = new SimpleConnectionBWConfig();
            bwConfig.getChannelBandwidth()[IBandwidthConfigure.OVERALL_CHANNEL] = 1024 * 1024;
            bwConfig.getChannelInitialBurst()[IBandwidthConfigure.OVERALL_CHANNEL] = 128 * 1024;
            streamConn.setBandwidthConfigure(bwConfig);
        }
        return super.appConnect(conn, params);
    }
