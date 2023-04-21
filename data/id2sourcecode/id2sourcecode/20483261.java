        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            LOG.error("ClientHanler#exceptionCaught");
            Throwable t = e.getCause();
            LOG.error(t.getMessage(), t);
            e.getFuture().setFailure(t);
            e.getChannel().close();
        }
