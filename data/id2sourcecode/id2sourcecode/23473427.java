    @Override
    public void append(Collection<WaveletDeltaRecord> deltas) throws PersistenceException {
        checkIsOpen();
        try {
            file.seek(file.length());
            WaveletDeltaRecord lastDelta = null;
            for (WaveletDeltaRecord delta : deltas) {
                index.addDelta(delta.transformed.getAppliedAtVersion(), delta.transformed.size(), file.getFilePointer());
                writeDelta(delta);
                lastDelta = delta;
            }
            file.getChannel().force(true);
            endVersion = lastDelta.transformed.getResultingVersion();
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }
