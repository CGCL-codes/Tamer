        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            log.warn("Caught exception from Netty channel handler.", e.getCause());
            ctx.getChannel().close();
        }
