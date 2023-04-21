    public void service() throws RequestException {
        Command command = requestGlobals.getCommand();
        ResultEncoder encoder = resultEncoders.get(command.getResponseEncoding());
        if (encoder == null) {
            throw new RequestException("Unknown response encoding: " + command.getResponseEncoding());
        }
        String results;
        try {
            results = encoder.encode(requestGlobals.getResult());
        } catch (EncoderException e) {
            log.error("Error encoding results", e);
            throw new RequestException("Error encoding results", e);
        }
        log.debug("Responding with: " + results);
        threadStateManager.setThreadStateAttachment(results);
        ByteBuffer buffer = Charset.defaultCharset().encode(results);
        requestGlobals.getNetworkRequest().storeResponseBuffer(buffer);
        networkEventThread.addChannelInterestOps(requestGlobals.getNetworkRequest().getChannel(), SelectionKey.OP_WRITE);
    }
