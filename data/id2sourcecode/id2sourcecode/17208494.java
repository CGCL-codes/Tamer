        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            e.getCause().printStackTrace();
            termReason = e.getCause().toString();
            if (ctx.getChannel().isWritable()) {
                TerminationNotice tn = TerminationNotice.newBuilder().setReason(termReason).build();
                ctx.getChannel().write(tn).addListener(ChannelFutureListener.CLOSE);
            } else {
                ctx.getChannel().close();
            }
        }
