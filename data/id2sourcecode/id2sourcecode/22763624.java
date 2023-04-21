    @Override
    public synchronized void process() {
        if (file == null) {
            log.info("Creating file object: " + getFileName());
            try {
                file = new FileInputStream(getFileName()).getChannel();
            } catch (FileNotFoundException ex) {
                log.error(getFileName(), ex);
                return;
            }
        }
        String rawData;
        try {
            rawData = readNextChunk(getNextChunkSize());
        } catch (EndOfFileException ex) {
            if (getRewindOnEOF()) {
                if (log.isDebugEnabled()) {
                    log.debug("Rewind file");
                }
                try {
                    file.position(0);
                } catch (IOException ex1) {
                    log.error("Cannot rewind", ex1);
                }
                process();
                return;
            } else {
                log.info("End of file reached: " + getFileName());
                if (JMeterContextService.getContext().getThread() != null) {
                    JMeterContextService.getContext().getThread().stop();
                }
                throw new RuntimeEOFException("End of file reached", ex);
            }
        } catch (IOException ex) {
            log.error("Error reading next chunk", ex);
            throw new RuntimeException("Error reading next chunk", ex);
        }
        final JMeterVariables vars = JMeterContextService.getContext().getVariables();
        if (vars != null) {
            vars.put(getVarName(), rawData);
        }
    }
