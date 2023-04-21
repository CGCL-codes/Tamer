    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws OpenR66ProtocolNetworkException {
        this.networkChannel = e.getChannel();
        try {
            if (DbConstant.admin.isConnected) {
                this.dbSession = new DbSession(DbConstant.admin, false);
            }
        } catch (GoldenGateDatabaseNoConnectionError e1) {
            logger.warn("Use default database connection");
            this.dbSession = DbConstant.admin.session;
        }
        logger.debug("Network Channel Connected: {} ", e.getChannel().getId());
    }
