    @Override
    protected byte[] processIO(SampleResult res) throws Exception {
        if (channel == null || !channel.isOpen()) {
            try {
                channel = (DatagramChannel) getChannel();
            } catch (IOException ex) {
                log.error("Cannot open channel", ex);
            }
        }
        ByteBuffer sendBuf = encoder.encode(getRequestData());
        channel.write(sendBuf);
        if (!isWaitResponse()) {
            res.latencyEnd();
            res.sampleEnd();
            return new byte[0];
        }
        try {
            ByteArrayOutputStream response = readResponse(res);
            return encoder.decode(response.toByteArray());
        } catch (IOException ex) {
            channel.close();
            throw ex;
        }
    }
