    public void testDecoder() throws Exception {
        FixFrameDecoder decoder = new FixFrameDecoder();
        final ChannelHandlerContext ctx = mockery.mock(ChannelHandlerContext.class);
        final ChannelStateEvent cse = mockery.mock(ChannelStateEvent.class);
        final ChannelBuffer buf = ChannelBuffers.dynamicBuffer(32);
        final MessageEvent e = mockery.mock(MessageEvent.class);
        final Sequence sequence = mockery.sequence("seq");
        mockery.checking(new Expectations() {

            {
                allowing(e).getChannel();
                allowing(e).getRemoteAddress();
                oneOf(ctx).sendUpstream(cse);
                inSequence(sequence);
                one(e).getMessage();
                will(returnValue(buf));
                inSequence(sequence);
                one(e).getMessage();
                will(returnValue(buf));
                inSequence(sequence);
            }
        });
        decoder.channelConnected(ctx, cse);
        if (log.isDebugEnabled()) log.debug("first fragment");
        buf.writeBytes(getBytes("garbage8=F"));
        decoder.messageReceived(ctx, e);
        if (log.isDebugEnabled()) log.debug("second fragment");
        buf.writeBytes(getBytes("IX.4.2\0019"));
        decoder.messageReceived(ctx, e);
        mockery.assertIsSatisfied();
        if (log.isDebugEnabled()) log.debug("third fragment");
        final DefaultMessageEventMatcher m = new DefaultMessageEventMatcher(49);
        mockery.checking(new Expectations() {

            {
                allowing(e).getChannel();
                allowing(e).getRemoteAddress();
                one(e).getMessage();
                will(returnValue(buf));
                one(ctx).sendUpstream(with(m));
                one(ctx).sendUpstream(cse);
            }
        });
        buf.writeBytes(getBytes("=12\00135=X\001108=30\00110=049\001"));
        decoder.messageReceived(ctx, e);
        decoder.channelDisconnected(ctx, cse);
        mockery.assertIsSatisfied();
    }
