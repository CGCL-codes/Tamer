    @Override
    public DataSample<Double> getSignal() {
        NDC.push("getSignal(" + address + ")");
        long start = System.currentTimeMillis();
        try {
            XBeeAddress64 xbeeAddress = Parser.parse(address.hardwareAddress);
            String channel = address.channel;
            ZNetRemoteAtRequest request = new ZNetRemoteAtRequest(xbeeAddress, "IS");
            AtCommandResponse rsp = (AtCommandResponse) container.sendSynchronous(request, 5000);
            logger.debug(channel + " response: " + rsp);
            if (rsp.isError()) {
                throw new IOException(channel + " + query failed, status: " + rsp.getStatus());
            }
            IoSample sample = new IoSample(rsp.getValue());
            logger.debug("sample: " + sample);
            return new DataSample<Double>(System.currentTimeMillis(), sourceName, signature, sample.getChannel(channel), null);
        } catch (Throwable t) {
            IOException secondary = new IOException("Unable to read " + address);
            secondary.initCause(t);
            throw new IllegalStateException("Not Implemented", t);
        } finally {
            logger.debug("complete in " + (System.currentTimeMillis() - start) + "ms");
            NDC.pop();
        }
    }
