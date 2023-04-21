        public void onRead(Object userContext, ByteBuffer[] buffers) {
            logger.debug("#read.cid:" + getChannelId());
            lastIo = System.currentTimeMillis();
            long length = BuffersUtil.remaining(buffers);
            client.asyncWrite(WRITE_REQUEST, buffers);
            client.responseBodyLength(length);
            asyncRead(READ_REQUEST);
            if (isUseProxy && isNeesClientRead) {
                client.asyncRead(READ_REQUEST);
                isNeesClientRead = false;
            }
        }
