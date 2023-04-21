    public void run() {
        try {
            if (Thread.currentThread() == reader) runReader(); else if (Thread.currentThread() == writer) runWriter();
        } catch (IOException e) {
            close();
        } catch (Exception e) {
        }
    }
