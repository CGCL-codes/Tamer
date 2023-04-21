    public void beginAsyncWrite() {
        if (isWriting) {
            ;
        } else {
            isWriting = true;
            Thread thread = new Thread(writer);
            thread.start();
        }
    }
