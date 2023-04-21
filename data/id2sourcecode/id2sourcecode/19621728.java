    public void onOOBControlMessage(IMessageComponent source, IPipe pipe, OOBControlMessage oobCtrlMsg) {
        if (!"ConnectionConsumer".equals(oobCtrlMsg.getTarget())) {
            return;
        }
        if ("pendingCount".equals(oobCtrlMsg.getServiceName())) {
            oobCtrlMsg.setResult(conn.getPendingMessages());
        } else if ("pendingVideoCount".equals(oobCtrlMsg.getServiceName())) {
            IClientStream stream = conn.getStreamByChannelId(video.getId());
            if (stream != null) {
                oobCtrlMsg.setResult(conn.getPendingVideoMessages(stream.getStreamId()));
            } else {
                oobCtrlMsg.setResult(0);
            }
        } else if ("chunkSize".equals(oobCtrlMsg.getServiceName())) {
            int newSize = (Integer) oobCtrlMsg.getServiceParamMap().get("chunkSize");
            if (newSize != chunkSize) {
                chunkSize = newSize;
                ChunkSize chunkSizeMsg = new ChunkSize(chunkSize);
                conn.getChannel((byte) 2).write(chunkSizeMsg);
            }
        }
    }
