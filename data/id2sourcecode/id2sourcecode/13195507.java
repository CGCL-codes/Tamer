    protected void addHandle(BpmFilter bpmFilter, String handle) {
        try {
            Channel channel = bpm.getChannel(handle);
            RecordFilter recordFilter = bpmFilter.filterForHandle(handle);
            _connectionHandler.requestToCorrelate(channel, recordFilter);
        } catch (NoSuchChannelException exception) {
            System.err.println(exception);
            exception.printStackTrace();
        }
    }
