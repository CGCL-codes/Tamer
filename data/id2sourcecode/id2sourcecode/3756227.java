        @Override
        public void onMessageHeader(IHttpConnection httpConnection) throws BufferUnderflowException, IOException {
            IHeader header = httpConnection.receiveMessageHeader();
            System.out.println(header);
            IResponseHeader responseHeader = httpConnection.createResponseHeader(200);
            responseHeader.addHeader("Content-Encoding", "text/plain");
            byte[] data = (System.currentTimeMillis() + "  Hello how are you?").getBytes();
            IWriteableChannel bodyHandle = httpConnection.sendChunkedMessageHeader(responseHeader);
            bodyHandle.transferFrom(getFileChannel(data));
            bodyHandle.close();
        }
