    @Override
    public void run() {
        if (!_Running.set()) {
            mChannel.writeNack("API configuration already in progress");
            return;
        }
        try {
            waitForMainWindow();
            configureAPI();
        } catch (Exception ex) {
            mChannel.writeNack(ex.getMessage());
        } finally {
            _Running.clear();
        }
    }
