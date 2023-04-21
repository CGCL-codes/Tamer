    public void writeThreadId(char[] c, int len) {
        String x = new String(c, 0, len);
        System.err.print(Thread.currentThread().getId() + " : " + x);
    }
