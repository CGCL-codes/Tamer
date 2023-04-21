    public ProductForColumnRange read() throws Exception {
        ProductForColumnRange product = delegate.read();
        ThreadUtils.writeThreadExecutionMessage("read", product);
        return product;
    }
