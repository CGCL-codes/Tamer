    @Override
    public Product read() throws Exception, UnexpectedInputException, ParseException {
        synchronized (count) {
            if (count.incrementAndGet() <= max) {
                Product product = new Product(String.valueOf(count.get()));
                ThreadUtils.writeThreadExecutionMessage("read", product);
                return product;
            } else {
                return null;
            }
        }
    }
