        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.messageReceived(ctx, e);
            System.out.println("-------- Channel messageRecieved " + System.currentTimeMillis());
            MessageEventBag bag = new MessageEventBag();
            bag.setBytes(e);
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            if (simulateConflict) {
                Header header = new ProtocolImpl(pf).read(new PropertiesConfiguration(), new DataInputStream(new ByteArrayInputStream(bag.getBytes())));
                if (counter++ == 10) {
                    counter = 0;
                    buffer.writeInt(ClientHandlerContext.STATUS_CONFLICT);
                    if (simulateConflictErrorPointer) {
                        buffer.writeLong(-1);
                    } else {
                        buffer.writeLong(filePointer);
                    }
                } else {
                    buffer.writeInt(ClientHandlerContext.STATUS_OK);
                    bagList.add(bag);
                }
                filePointer = header.getFilePointer();
            } else {
                buffer.writeInt(ClientHandlerContext.STATUS_OK);
                bagList.add(bag);
            }
            ChannelFuture future = e.getChannel().write(buffer);
            future.addListener(ChannelFutureListener.CLOSE);
        }
