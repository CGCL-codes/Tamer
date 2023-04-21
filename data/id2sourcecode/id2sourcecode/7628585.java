    @SuppressWarnings("unchecked")
    public void doSth() {
        write.addClassToCache(read.readWordToCache());
        try {
            write.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
