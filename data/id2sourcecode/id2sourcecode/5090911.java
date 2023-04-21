        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            Throwable cause = e.getCause();
            log.warn(cause.getMessage());
            e.getChannel().close();
        }
