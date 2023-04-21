    @Override
    public void messageReceived(IoSession session, Object message) {
        logger.debug("CLIENT - Message received: " + session);
        IoBuffer buf = (IoBuffer) message;
        try {
            if (file == null) {
                file = File.createTempFile("http", ".html");
                logger.info("Writing request result to " + file.getAbsolutePath());
                wChannel = new FileOutputStream(file, false).getChannel();
            }
            wChannel.write(buf.buf());
        } catch (IOException e) {
        }
    }
