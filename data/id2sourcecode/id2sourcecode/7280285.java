    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.debug("Network Channel Exception: {}", e.getChannel().getId(), e.getCause());
        if (e.getCause() instanceof ReadTimeoutException) {
            ReadTimeoutException exception = (ReadTimeoutException) e.getCause();
            logger.error("ReadTimeout so Will close NETWORK channel {}", exception.getMessage());
            ChannelUtils.close(e.getChannel());
            return;
        }
        if (e.getCause() instanceof BindException) {
            logger.debug("BindException");
            try {
                Thread.sleep(Configuration.WAITFORNETOP);
            } catch (InterruptedException e1) {
            }
            ChannelUtils.close(e.getChannel());
            return;
        }
        OpenR66Exception exception = OpenR66ExceptionTrappedFactory.getExceptionFromTrappedException(e.getChannel(), e);
        if (exception != null) {
            if (exception instanceof OpenR66ProtocolBusinessNoWriteBackException) {
                if (NetworkTransaction.getNbLocalChannel(e.getChannel()) > 0) {
                    logger.debug("Network Channel Exception: {} {}", e.getChannel().getId(), exception.getMessage());
                }
                logger.debug("Will close NETWORK channel");
                try {
                    Thread.sleep(Configuration.WAITFORNETOP);
                } catch (InterruptedException e1) {
                    ChannelUtils.close(e.getChannel());
                    Thread.currentThread().interrupt();
                }
                ChannelUtils.close(e.getChannel());
                return;
            } else if (exception instanceof OpenR66ProtocolNoConnectionException) {
                logger.debug("Connection impossible with NETWORK channel {}", exception.getMessage());
                Channels.close(e.getChannel());
                return;
            } else {
                logger.debug("Network Channel Exception: {} {}", e.getChannel().getId(), exception.getMessage());
            }
            final ConnectionErrorPacket errorPacket = new ConnectionErrorPacket(exception.getMessage(), null);
            writeError(e.getChannel(), ChannelUtils.NOCHANNEL, ChannelUtils.NOCHANNEL, errorPacket);
            try {
                Thread.sleep(Configuration.WAITFORNETOP);
            } catch (InterruptedException e1) {
            }
            logger.debug("Will close NETWORK channel: {}", exception.getMessage());
            ChannelUtils.close(e.getChannel());
        } else {
            return;
        }
    }
