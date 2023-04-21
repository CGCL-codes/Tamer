    public void processMessage(Message msg) throws ProcessorException {
        if (msg.getHeader(Message.TYPE).equals(Message.TYPE_ANALYZE)) {
            Link inLink = msg.getMessageContext().getInBoundLink();
            if (msg.getHeader(Message.MESSAGE_NAME).equals(MESSAGE_NAME_PONG)) {
                String ts = msg.getHeader(Message.TIME_STAMP);
                long latency = (System.currentTimeMillis() - Long.parseLong(ts)) / 2;
                logger.debug("latency: " + latency);
                if (updateCost) {
                    inLink.setCost(QOSCLASS_LATENCY, (int) latency);
                }
                meanLatency = ((meanLatency * samples) + latency) / (samples + 1);
                samples++;
            } else if (msg.getHeader(Message.MESSAGE_NAME).equals(MESSAGE_NAME_PING)) {
                msg.setHeader(Message.MESSAGE_NAME, MESSAGE_NAME_PONG);
                inLink.getChannel().getOutLink().processMessage(msg);
            }
            throw new ProcessorInterruptException();
        }
    }
