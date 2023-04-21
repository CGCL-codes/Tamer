    public void write(List<? extends Product> items) throws Exception {
        ThreadUtils.writeThreadExecutionMessage("write", items);
        for (Product product : items) {
            getJdbcTemplate().update("update product set processed=? where id=?", true, product.getId());
        }
    }
