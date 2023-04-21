    private void sort() throws Exception {
        if (end - start <= 1) {
            return;
        }
        int middle = start + (end - start) / 2;
        Future<Boolean> left = pool.submit(new Sort<T>(array, pool, start, middle));
        Future<Boolean> right = pool.submit(new Sort<T>(array, pool, middle, end));
        if (left.get() && right.get()) {
            merge(middle);
        }
    }
