    public void run() {
        while (activate) {
            try {
                Message msg = topicSuscriber.receive(1000);
                if (msg != null) {
                    multiplexer.onMessage(msg);
                }
            } catch (JMSException e) {
                logger.error(multiplexer.getChannelName() + "| " + e.getMessage() + "|");
            }
        }
    }
