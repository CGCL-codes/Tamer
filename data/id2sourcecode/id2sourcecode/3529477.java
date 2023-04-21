    private void send() {
        try {
            Message msg = initMessage();
            msg.setContent(messageBuffer.toString(), "text/plain");
            Transport.send(msg);
        } catch (Exception e) {
            log4j.error("Unable to send message", e);
        }
    }
