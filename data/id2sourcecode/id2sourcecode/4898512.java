    public boolean hasWork() {
        return reader.needMoreData() || writer.hasMoreData();
    }
