    private static final void writePrefix() throws IOException {
        logWriter.write("{" + Thread.currentThread().getName() + "} ");
        int mill = (int) ((System.currentTimeMillis() + TZ_OFF) % (24 * 3600000));
        int secs = mill / 1000;
        int mins = secs / 60;
        int hours = mins / 60;
        logWriter.write("[" + fill2(hours) + ':' + fill2(mins - hours * 60) + ':' + fill2(secs - mins * 60) + '.' + fill3(mill - secs * 1000) + "] ");
    }
