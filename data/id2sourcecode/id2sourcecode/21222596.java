        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            System.out.println("Server Exception Caught");
            e.getCause().printStackTrace();
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            buffer.writeInt(500);
            ChannelFuture future = e.getChannel().write(buffer);
            future.addListener(ChannelFutureListener.CLOSE);
        }
