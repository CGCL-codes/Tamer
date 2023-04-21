    private void write() {
        final ArrayHashSet copy = new ArrayHashSet(_everybody.size());
        copy.addAll(_everybody);
        _saver.setData(copy);
        Thread writer = new Thread(_saver);
        writer.setDaemon(false);
        writer.setName("gnutella.net saver");
        writer.setPriority(Thread.MIN_PRIORITY);
        writer.start();
    }
