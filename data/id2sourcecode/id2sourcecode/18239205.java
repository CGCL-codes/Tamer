    void registerSelector(Selector selector, int ops) throws IOException {
        if (isOpen()) {
            getChannel().configureBlocking(false);
            key = getChannel().register(selector, ops, this);
        }
    }
