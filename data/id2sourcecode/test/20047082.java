        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            Channel channel = e.getChannel();
            boolean sentLines = false;
            ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer(1024);
            ChannelBufferOutputStream output = new ChannelBufferOutputStream(channelBuffer);
            protocol.send(clientHandlerContext.getHeader(), clientHandlerContext.getFileLineStreamer().getCodec(), output);
            sentLines = clientHandlerContext.getFileLineStreamer().streamContent(clientHandlerContext.getIntermediatePointer(), clientHandlerContext.getReader(), output);
            if (sentLines) {
                int messageLen = channelBuffer.readableBytes();
                ChannelBuffer messageLenBuffer = ChannelBuffers.buffer(4);
                messageLenBuffer.writeInt(messageLen);
                ChannelBuffer messageBuffer = ChannelBuffers.wrappedBuffer(messageLenBuffer, channelBuffer);
                channel.write(messageBuffer);
            } else {
                channel.close();
                if (!exhanged.getAndSet(true)) {
                    exchanger.exchange(clientHandlerContext, sendTimeOut, TimeUnit.MILLISECONDS);
                }
            }
            super.channelConnected(ctx, e);
        }
