        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.messageReceived(ctx, e);
            System.out.println("-------- Channel messageRecieved " + System.currentTimeMillis());
            MessageEventBag bag = new MessageEventBag();
            bag.setBytes(e);
            bagList.add(bag);
            Protocol p = new ProtocolImpl(pf);
            Header header = p.read(conf, new DataInputStream(new ByteArrayInputStream(bag.getBytes())));
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            if (counter++ == 10) {
                counter = 0;
                buffer.writeInt(409);
                buffer.writeLong(filePointer);
            } else {
                buffer.writeInt(200);
            }
            filePointer = header.getFilePointer();
            ChannelFuture future = e.getChannel().write(buffer);
            future.addListener(ChannelFutureListener.CLOSE);
        }
