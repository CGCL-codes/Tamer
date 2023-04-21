    @Override
    public void write(List<? extends ProductForColumnRange> items) throws Exception {
        ThreadUtils.writeThreadExecutionMessage("write", items);
        for (ProductForColumnRange product : items) {
            processProduct(product);
        }
    }
