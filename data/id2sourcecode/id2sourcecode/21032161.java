    public Monolith(File store, boolean rewrite) throws IOException {
        _store = store;
        boolean newStore = false;
        if (!store.exists() || store.length() == 0) {
            newStore = true;
        }
        _channel = new RandomAccessFile(_store, "rw").getChannel();
        if (rewrite) _channel.truncate(0L);
        _index = new AllocationTable(_channel);
        if (newStore || rewrite) {
            sync();
        }
        _index.loadTable();
        truncateEmptySpace();
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
