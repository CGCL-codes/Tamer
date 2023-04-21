        public void onReadPlain(Object userContext, ByteBuffer[] buffers) {
            logger.debug("#readPlain server.cid:" + getChannelId());
            long length = BuffersUtil.remaining(buffers);
            client.asyncWrite(null, buffers);
            client.responseBodyLength(length);
            server.asyncRead(null);
            lastIo = System.currentTimeMillis();
        }
