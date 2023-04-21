        @Override
        public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
            LOG.debug("ClientHanler#messageReceived");
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            DNSMessage msg = new DNSMessage(buffer);
            msg.header().id(this.original.header().id());
            ChannelBuffer newone = ChannelBuffers.buffer(buffer.capacity());
            msg.write(newone);
            newone.resetReaderIndex();
            this.originalChannel.write(newone, this.originalAddress).addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    e.getChannel().close();
                }
            });
        }
