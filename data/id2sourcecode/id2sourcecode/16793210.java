        public void onConnected(Object requestId) {
            logger.debug("#connect.cid:" + getChannelId());
            HeaderParser requestParser = getRequestHeader();
            isConnected = true;
            if (isUseProxy) {
                OutputStream os = null;
                try {
                    os = server.getOutputStream(WRITE_REQUEST);
                    requestParser.writeHeader(os);
                    isNeesClientRead = true;
                } finally {
                    try {
                        os.close();
                    } catch (IOException ignore) {
                    }
                }
            } else {
                client.setStatusCode("200");
                client.asyncWrite(WRITE_REQUEST, BuffersUtil.toByteBufferArray(ByteBuffer.wrap(ProxyOkResponse)));
                client.asyncRead(READ_REQUEST);
                ByteBuffer[] body = requestParser.getBodyBuffer();
                if (body != null) {
                    server.asyncWrite(WRITE_REQUEST, body);
                }
            }
            server.asyncRead(READ_REQUEST);
        }
