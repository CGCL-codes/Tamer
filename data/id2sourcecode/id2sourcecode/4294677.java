    long sendBlock(DataOutputStream out, OutputStream baseStream, BlockTransferThrottler throttler) throws IOException {
        if (out == null) {
            throw new IOException("out stream is null");
        }
        this.throttler = throttler;
        long initialOffset = offset;
        long totalRead = 0;
        OutputStream streamForSendChunks = out;
        try {
            try {
                checksum.writeHeader(out);
                if (chunkOffsetOK) {
                    out.writeLong(offset);
                }
                out.flush();
            } catch (IOException e) {
                throw ioeToSocketException(e);
            }
            int maxChunksPerPacket;
            int pktSize = DataNode.PKT_HEADER_LEN + SIZE_OF_INTEGER;
            if (transferToAllowed && !verifyChecksum && baseStream instanceof SocketOutputStream && blockIn instanceof FileInputStream) {
                FileChannel fileChannel = ((FileInputStream) blockIn).getChannel();
                blockInPosition = fileChannel.position();
                streamForSendChunks = baseStream;
                maxChunksPerPacket = (Math.max(BUFFER_SIZE, MIN_BUFFER_WITH_TRANSFERTO) + bytesPerChecksum - 1) / bytesPerChecksum;
                pktSize += checksumSize * maxChunksPerPacket;
            } else {
                maxChunksPerPacket = Math.max(1, (BUFFER_SIZE + bytesPerChecksum - 1) / bytesPerChecksum);
                pktSize += (bytesPerChecksum + checksumSize) * maxChunksPerPacket;
            }
            ByteBuffer pktBuf = ByteBuffer.allocate(pktSize);
            while (endOffset > offset) {
                long len = sendChunks(pktBuf, maxChunksPerPacket, streamForSendChunks);
                offset += len;
                totalRead += len + ((len + bytesPerChecksum - 1) / bytesPerChecksum * checksumSize);
                seqno++;
            }
            try {
                out.writeInt(0);
                out.flush();
            } catch (IOException e) {
                throw ioeToSocketException(e);
            }
        } finally {
            if (clientTraceFmt != null) {
                ClientTraceLog.info(String.format(clientTraceFmt, totalRead));
            }
            close();
        }
        blockReadFully = (initialOffset == 0 && offset >= blockLength);
        return totalRead;
    }
