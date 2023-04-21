    public static void exceptionCaught(final ExceptionEvent e) {
        if (e.getCause() instanceof ClosedChannelException) {
            logger.info("exception: {}", e);
        } else if (e.getCause() instanceof IOException) {
            logger.info("exception: {}", e.getCause().getMessage());
        } else {
            logger.warn("exception: {}", e.getCause());
        }
        if (e.getChannel().isOpen()) {
            e.getChannel().close();
        }
    }
