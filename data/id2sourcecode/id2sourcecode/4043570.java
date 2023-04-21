    @Override
    public void write(List<? extends Product> items) throws Exception {
        ThreadUtils.writeThreadExecutionMessage("write", items);
        for (Product product : items) {
            processProduct(product);
        }
    }
