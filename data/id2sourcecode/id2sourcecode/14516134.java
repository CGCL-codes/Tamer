    private void finish() {
        if (executor != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Shutting down threadpool for index writer " + MAX_WAIT_BEFORE_SHUTDOWN + ", tasks remaining : " + executor.getTaskCount() + ", tasks executed: " + executor.getCompletedTaskCount());
            }
            executor.shutdown();
            try {
                executor.awaitTermination(MAX_WAIT_BEFORE_SHUTDOWN, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Completed shutdown threadpool for index writer");
            }
            executor = null;
        }
    }
