    private void writeThreadRunner() throws InterruptedException {
        ByteBuffer outputBuffer = allocator.allocate(BUFFER_SIZE);
        long heartbeatIntervalMillis = spec.getHeartbeatInterval();
        while (true) {
            checkWriteInterrupted();
            long lastBufferTimestamp = System.currentTimeMillis();
            while (writeQueue.size() == 0) {
                if (System.currentTimeMillis() - lastBufferTimestamp > heartbeatIntervalMillis) {
                    break;
                }
                synchronized (isWriteRunning) {
                    isWriteRunning.wait(SLEEP_DELAY);
                }
                checkWriteInterrupted();
            }
            AbstractRequest job = writeQueue.poll();
            outputBuffer.clear();
            if (job != null) {
                pendingJobs.put(job.getMessage().getRequestId(), job);
                sessionMarshaler.marshal(job.getMessage(), outputBuffer);
            } else {
                HeartbeatRequest hb = new HeartbeatRequest(0);
                sessionMarshaler.marshal(hb, outputBuffer);
            }
            outputBuffer.flip();
            try {
                connector.write(outputBuffer);
            } catch (IOException ex) {
                error.compareAndSet(null, newError("io error in writer: " + ex.getMessage()));
                throw new InterruptedException();
            }
        }
    }
