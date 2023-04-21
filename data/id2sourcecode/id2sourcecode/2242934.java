    public void run() {
        boolean done = false;
        long startTime = System.currentTimeMillis();
        while ((startTime + timeout) > (System.currentTimeMillis())) {
            Random rand = new Random();
            try {
                if (systemBus == null) {
                    log.error("WriteToBusThread: SystemBus null");
                    throw new java.lang.RuntimeException("WriteToBusThread: SystemBus is still null.");
                }
                systemBus.write(entry, javaSpaceTransaction, Long.MAX_VALUE);
                log.info("WriteToBusThread: wrote: " + entry.toString());
                done = true;
                break;
            } catch (Exception e) {
                try {
                    wait(rand.nextInt((int) timeout / 3));
                } catch (Exception e1) {
                }
            }
        }
        if (!done) {
            log.error("ERROR: could not write entry into systembus. Given up");
            throw new java.lang.RuntimeException("writeToBusThread: Could not write into systembus for some reason");
        }
    }
