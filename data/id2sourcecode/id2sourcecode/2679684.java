    public void enqueue(IElement req) throws SinkException {
        AFileRequest areq = (AFileRequest) req;
        if (closed) {
            throw new SinkClosedException("Sink is closed");
        }
        if (readOnly && (areq instanceof AFileWriteRequest)) {
            throw new BadElementException("Cannot enqueue write request for read-only file", areq);
        }
        areq.afile = afile;
        try {
            eventQ.enqueue(areq);
        } catch (SinkException se) {
            throw new InternalError("AFileTPImpl.enqueue got SinkException - this should not happen, please contact <mdw@cs.berkeley.edu>");
        }
        if (eventQ.size() == 1) {
            tm.fileReady(this);
        }
    }
