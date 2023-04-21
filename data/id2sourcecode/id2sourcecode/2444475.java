    private void writeQueue(Collection entity) {
        while (true) {
            try {
                if (entityQueue.offer(entity, TIMEOUT, TimeUnit.SECONDS)) {
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!writerThread.isAlive()) {
                throw new RuntimeException("writerThread was died.");
            }
        }
    }
