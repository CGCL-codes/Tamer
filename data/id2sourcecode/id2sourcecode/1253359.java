    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, MessageEvent messageEvent) throws Exception {
        ChannelBuffer in = (ChannelBuffer) messageEvent.getMessage();
        try {
            if (status.state == SessionStatus.State.PROCESSING) {
                List<String> pieces = new ArrayList<String>(6);
                int pos = in.bytesBefore(space);
                do {
                    if (pos != -1) {
                        pieces.add(in.toString(in.readerIndex(), pos, USASCII));
                        in.skipBytes(pos + 1);
                    }
                } while ((pos = in.bytesBefore(space)) != -1);
                pieces.add(in.toString(USASCII));
                processLine(pieces, messageEvent.getChannel(), channelHandlerContext);
            } else if (status.state == SessionStatus.State.PROCESSING_MULTILINE) {
                ChannelBuffer slice = in.copy();
                byte[] payload = slice.array();
                in.skipBytes(in.readableBytes());
                continueSet(messageEvent.getChannel(), status, payload, channelHandlerContext);
            } else {
                throw new InvalidProtocolStateException("invalid protocol state");
            }
        } finally {
            if (status.state != SessionStatus.State.WAITING_FOR_DATA) status.ready();
        }
    }
