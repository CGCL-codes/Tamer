    private boolean selectorsMakeSense() {
        for (MemcachedNode qa : locator.getAll()) {
            if (qa.getSk() != null && qa.getSk().isValid()) {
                if (qa.getChannel().isConnected()) {
                    int sops = qa.getSk().interestOps();
                    int expected = 0;
                    if (qa.hasReadOp()) {
                        expected |= SelectionKey.OP_READ;
                    }
                    if (qa.hasWriteOp()) {
                        expected |= SelectionKey.OP_WRITE;
                    }
                    if (qa.getBytesRemainingToWrite() > 0) {
                        expected |= SelectionKey.OP_WRITE;
                    }
                    assert sops == expected : "Invalid ops:  " + qa + ", expected " + expected + ", got " + sops;
                } else {
                    int sops = qa.getSk().interestOps();
                    assert sops == SelectionKey.OP_CONNECT : "Not connected, and not watching for connect: " + sops;
                }
            }
        }
        getLogger().debug("Checked the selectors.");
        return true;
    }
