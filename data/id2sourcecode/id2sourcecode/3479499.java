    @Override
    public String dumpPipeline() {
        DequePayload payload = deque.peekFirst();
        return getClass().getName() + ": " + (payload == null ? null : payload.reader) + "->" + writer;
    }
