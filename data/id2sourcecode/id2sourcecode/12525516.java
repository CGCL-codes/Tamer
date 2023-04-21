    public void compact() {
        System.arraycopy(buf, reader, buf, 0, writer -= reader);
        reader = 0;
    }
