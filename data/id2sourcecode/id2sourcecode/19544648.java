        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            Throwable t = e.getCause();
            if (t != null) {
                t.printStackTrace();
            }
            e.getChannel().close();
        }
