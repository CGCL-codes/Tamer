    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        logger.debug("Local Client Channel Exception: {}", e.getChannel().getId(), e.getCause());
        if (localChannelReference == null) {
            initLocalClientHandler(e.getChannel());
        }
        if (localChannelReference != null) {
            OpenR66Exception exception = OpenR66ExceptionTrappedFactory.getExceptionFromTrappedException(e.getChannel(), e);
            localChannelReference.sessionNewState(ERROR);
            if (exception != null) {
                if (exception instanceof OpenR66ProtocolShutdownException) {
                    Thread thread = new Thread(new ChannelUtils(), "R66 Shutdown Thread");
                    thread.setDaemon(true);
                    thread.start();
                    logger.debug("Will close channel");
                    Channels.close(e.getChannel());
                    return;
                } else if (exception instanceof OpenR66ProtocolBusinessNoWriteBackException) {
                    logger.error("Will close channel", exception);
                    Channels.close(e.getChannel());
                    return;
                } else if (exception instanceof OpenR66ProtocolNoConnectionException) {
                    logger.error("Will close channel", exception);
                    Channels.close(e.getChannel());
                    return;
                }
                final ErrorPacket errorPacket = new ErrorPacket(exception.getMessage(), ErrorCode.RemoteError.getCode(), ErrorPacket.FORWARDCLOSECODE);
                ChannelUtils.writeAbstractLocalPacket(localChannelReference, errorPacket).awaitUninterruptibly();
                if (!localChannelReference.getFutureRequest().isDone()) {
                    localChannelReference.invalidateRequest(new R66Result(exception, localChannelReference.getSession(), true, ErrorCode.Internal, null));
                }
            } else {
                return;
            }
        }
        logger.debug("Will close channel");
        ChannelUtils.close(e.getChannel());
    }
