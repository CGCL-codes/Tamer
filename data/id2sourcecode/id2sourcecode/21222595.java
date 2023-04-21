        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.messageReceived(ctx, e);
            System.out.println("-------- Server  Channel messageRecieved " + System.currentTimeMillis());
            if (induceError.get()) {
                System.out.println("Inducing Error in Server messageReceived method");
                throw new IOException("Induced error ");
            }
            MessageEventBag bag = new MessageEventBag();
            bag.setBytes(e);
            bagList.add(bag);
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            buffer.writeInt(200);
            ChannelFuture future = e.getChannel().write(buffer);
            future.addListener(ChannelFutureListener.CLOSE);
        }
