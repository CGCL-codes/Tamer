    public void shutdown() throws IOException {
        shutDown = true;
        Selector s = selector.wakeup();
        assert s == selector : "Wakeup returned the wrong selector.";
        for (MemcachedNode qa : locator.getAll()) {
            if (qa.getChannel() != null) {
                qa.getChannel().close();
                qa.setSk(null);
                if (qa.getBytesRemainingToWrite() > 0) {
                    getLogger().warn("Shut down with %d bytes remaining to write", qa.getBytesRemainingToWrite());
                }
                getLogger().debug("Shut down channel %s", qa.getChannel());
            }
        }
        selector.close();
        getLogger().debug("Shut down selector %s", selector);
    }
