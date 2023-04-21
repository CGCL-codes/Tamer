    @Override
    public Object getSource(int index, ContentType contentType) {
        switch(contentType) {
            case ByteStream:
            case XML:
            case ResultSet:
                throw new XformationException("Content type: " + contentType + " is not supported.");
        }
        if (readerForFaucets.get(index) != null) return readerForFaucets.get(index);
        this.writers.set(index, new PipedWriter());
        try {
            this.readerForFaucets.set(index, new PipedReader(this.writers.get(index)));
        } catch (IOException e) {
            throw new XformationException("Unable to create hooked piped reader/writer", e);
        }
        if (logger.isTraceEnabled()) logger.trace("Created for index " + index + ", reader " + readerForFaucets.get(index) + " linked with writer " + writers.get(index));
        return readerForFaucets.get(index);
    }
