    public Thread write(BufferedImage bi) {
        if (bi == null) {
            return null;
        }
        mBI = bi;
        Thread t = new Thread(this, "Concurrent Image Writer - " + mOutput.getName());
        t.start();
        return t;
    }
