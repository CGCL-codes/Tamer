    @Override
    public int write(ByteBuffer src) throws IOException {
        int write;
        if (!this.task.isCancelled() && !this.task.isDone()) {
            write = this.out.getChannel().write(src);
        } else {
            write = 0;
        }
        if (write > 0) {
            this.listener.exportProgress(write);
        }
        return write;
    }
