            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
                log.warn("Exception in gateway: " + e.getCause().toString());
                log.warn("Closing channel.");
                e.getChannel().close();
            }
