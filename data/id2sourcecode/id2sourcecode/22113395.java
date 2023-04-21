    public void close() {
        LOG.info("Flushing annotation blacklists");
        Utils.writeAll(new File(db, "UNREADABLE"), unreadables);
        Utils.writeAll(new File(db, "UNWRITEABLE"), unwriteables);
    }
