    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        ChannelBuffer buff = (ChannelBuffer) e.getMessage();
        ChannelBufferInputStream channelInput = new ChannelBufferInputStream(buff);
        final FileTrackingStatus fileStatus = objMapper.readValue(channelInput, FileTrackingStatus.class);
        SocketAddress remoteAddressObj = e.getRemoteAddress();
        String collectorAddress = (remoteAddressObj == null) ? "unknown" : remoteAddressObj.toString();
        final SyncPointer syncPointer = getAndLockResource(fileStatus, (InetSocketAddress) e.getRemoteAddress());
        try {
            ChannelBuffer buffer = null;
            if (syncPointer == null) {
                buffer = ChannelBuffers.buffer(CONFLICT_MESSAGE.length + 8);
                buffer.writeInt(CONFLICT_MESSAGE.length + 4);
                buffer.writeInt(409);
                buffer.writeBytes(CONFLICT_MESSAGE);
                fileTrackerHistoryMemory.addAsyncToHistory(new FileTrackerHistoryItem(new Date(), fileStatus.getAgentName(), collectorAddress, FileTrackerHistoryItem.STATUS.ALREADY_LOCKED));
            } else {
                String msg = objMapper.writeValueAsString(syncPointer);
                byte[] msgBytes = msg.getBytes();
                int msgLen = msgBytes.length + 4;
                buffer = ChannelBuffers.dynamicBuffer(msgLen);
                buffer.writeInt(msgLen);
                buffer.writeInt(200);
                buffer.writeBytes(msgBytes, 0, msgBytes.length);
                FileTrackerHistoryItem.STATUS status = (syncPointer.getFilePointer() == fileStatus.getFilePointer()) ? FileTrackerHistoryItem.STATUS.OK : FileTrackerHistoryItem.STATUS.OUTOF_SYNC;
                fileTrackerHistoryMemory.addAsyncToHistory(new FileTrackerHistoryItem(new Date(), fileStatus.getAgentName(), collectorAddress, status));
                if (LOG.isDebugEnabled()) {
                    LOG.debug("LOCK( " + syncPointer.getLockId() + ") - " + fileStatus.getAgentName() + "." + fileStatus.getLogType() + "." + fileStatus.getFileName());
                }
            }
            ChannelFuture future = e.getChannel().write(buffer);
            future.addListener(ChannelFutureListener.CLOSE);
        } catch (Exception t) {
            LOG.error("ERROR MAKING LOCK " + fileStatus.getAgentName() + "." + fileStatus.getLogType() + "." + fileStatus.getFileName());
            throw t;
        }
    }
