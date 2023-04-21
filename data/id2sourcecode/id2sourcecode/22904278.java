    @Override
    public Object getSource(ContentType contentType) {
        switch(contentType) {
            case XML:
            case ByteStream:
            case ResultSet:
                throw new XformationException("Content type: " + contentType + " is not supported.");
        }
        if (readerForFaucet != null) return readerForFaucet;
        this.writer = new PipedWriter();
        try {
            this.readerForFaucet = new PipedReader(this.writer);
        } catch (IOException e) {
            throw new XformationException("Unable to create hooked piped streams", e);
        }
        if (logger.isTraceEnabled()) logger.trace("Created reader " + readerForFaucet + " linked with writer " + writer);
        return readerForFaucet;
    }
