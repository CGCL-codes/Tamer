    @Override
    public Object consume(ISink previousSink) {
        if (!hookedUpFaucet) throw new XformationException("Pipeline item has not been set up " + "correctly: faucet has not been set");
        if (!consumptionGate.getAndSet(false)) return null;
        consumptionSemaphore.acquireUninterruptibly(sinks.size());
        if (faucet instanceof IPipelineItem) ((IPipelineItem) faucet).consume(this);
        if (this instanceof ISinkTemplate) {
            if (!((ISinkTemplate) this).isClone()) {
                return null;
            }
        }
        boolean hookedUpSinks = true;
        for (Boolean hookedUpSink : hookedUpSinkFlags) {
            if (!hookedUpSink) {
                hookedUpSinks = false;
                break;
            }
        }
        if (!hookedUpSinks) throw new XformationException("Pipeline item has not been set up " + "correctly: sinks has not been all set");
        reader = (Reader) faucet.getSource(ContentType.CharStream);
        for (int i = 0; i < sinks.size(); i++) getSource(i, ContentType.CharStream);
        ExecutorsHelper.getInstance().executeInProc(new Runnable() {

            @Override
            public void run() {
                try {
                    int count;
                    char[] buffer = new char[8 * 1024];
                    if (logger.isTraceEnabled()) logger.trace("Using reader " + reader + " for the stream transformation.");
                    while ((count = reader.read(buffer)) != -1) {
                        for (Writer writer : writers) {
                            if (logger.isTraceEnabled()) {
                                logger.trace("Read " + new String(buffer, 0, count) + " from reader " + reader + ", writing to writer " + writer);
                            }
                            writer.write(buffer, 0, count);
                        }
                    }
                    for (Writer writer : writers) writer.close();
                    logger.debug("Executable for the stream pipeline will now exit.");
                } catch (Throwable t) {
                    logger.error("Error during pipeline thread execution.", t);
                }
            }
        });
        return null;
    }
