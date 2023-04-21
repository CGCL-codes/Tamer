    private void handleIncommingUriDownload(String requestLine) throws IOException {
        try {
            DestAddress localAddress = servent.getLocalAddress();
            if (!socket.getRemoteAddress().isLocalHost(localAddress)) {
                return;
            }
            socket.getChannel().write(BufferCache.OK_BUFFER);
        } finally {
            IOUtil.closeQuietly(socket);
        }
        String uriToken = requestLine.substring(URI_DOWNLOAD_PREFIX.length() + 1);
        PhexEventService eventService = Phex.getEventService();
        eventService.publish(PhexEventTopics.Incoming_Uri, uriToken);
    }
