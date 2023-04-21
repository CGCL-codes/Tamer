    public void run() {
        if (!_Running.set()) {
            writeNack("STOP already in progress");
            return;
        }
        try {
            writeInfo("Closing IBController");
            stop((mGateway) ? "Close" : "Exit");
        } catch (Exception ex) {
            writeNack(ex.getMessage());
        } finally {
            _Running.clear();
        }
    }
