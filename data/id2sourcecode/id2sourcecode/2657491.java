    public void writeAll(Collection<T> beans) {
        for (T bean : beans) {
            String[] nextLine = rowMapper.getRow(bean);
            writeNext(nextLine);
        }
        close();
    }
