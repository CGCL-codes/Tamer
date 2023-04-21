    @Override
    public Object consume(ISink previousSink) {
        if (!hookedUpFaucet) throw new XformationException("Pipeline item has not been set up " + "correctly: faucet has not been set");
        if (faucet instanceof IPipelineItem) ((IPipelineItem) faucet).consume(this);
        if (this instanceof ISinkTemplate) {
            if (!((ISinkTemplate) this).isClone()) {
                return null;
            }
        }
        if (!hookedUpSink) throw new XformationException("Pipeline item has not been set up " + "correctly: sink has not been set");
        reader = (Reader) faucet.getSource(ContentType.CharStream);
        bufferedReader = new BufferedReader(reader);
        getSource(ContentType.CharStream);
        ExecutorsHelper.getInstance().executeInProc(new Runnable() {

            @Override
            public void run() {
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String transformedLine = transformLine(line);
                        if (logger.isTraceEnabled()) logger.trace("Read '" + line + "' from reader" + bufferedReader + ", writing '" + transformedLine + "' to writer " + writer);
                        writer.write(transformedLine);
                    }
                    String transformedLine = transformLine(line);
                    logger.trace("Writing final '" + transformedLine + "' to writer " + writer);
                    writer.write(transformedLine);
                    writer.close();
                    logger.debug("Executable for the line reader pipeline will now exit.");
                } catch (Throwable t) {
                    logger.error("Error during pipeline thread execution.", t);
                }
            }
        });
        return null;
    }
