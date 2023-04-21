    public void dump(Writer writer) throws IOException {
        ExpectationContainer threadContainer = getThreadSpecificBuilder();
        threadContainer.dump(writer);
    }
