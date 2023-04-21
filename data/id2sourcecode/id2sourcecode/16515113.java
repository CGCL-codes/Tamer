    public void setIndex(int readerIndex, int writerIndex) {
        if (readerIndex < 0 || readerIndex > writerIndex || writerIndex > capacity()) {
            throw new IndexOutOfBoundsException();
        }
        this.readerIndex = readerIndex;
        this.writerIndex = writerIndex;
    }
