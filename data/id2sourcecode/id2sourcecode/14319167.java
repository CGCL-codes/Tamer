    protected void startWriter() {
        thread = new Thread(this, getClass().getName() + " writer");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }
