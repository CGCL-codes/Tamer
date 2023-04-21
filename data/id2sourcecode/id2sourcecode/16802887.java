    @Override
    public void sampleOccurred(SampleEvent evt) {
        if (fileChannel == null || !fileChannel.isOpen()) {
            if (log.isWarnEnabled()) {
                log.warn("File writer is closed! Maybe test has already been stopped");
            }
            return;
        }
        ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 10);
        for (int n = 0; n < compiledConsts.length; n++) {
            if (compiledConsts[n] != null) {
                synchronized (compiledConsts) {
                    buf.put(compiledConsts[n].duplicate());
                }
            } else {
                if (!appendSampleResultField(buf, evt.getResult(), compiledFields[n])) {
                    appendSampleVariable(buf, evt, compiledVars[n]);
                }
            }
        }
        buf.flip();
        try {
            syncWrite(buf);
        } catch (IOException ex) {
            log.error("Problems writing to file", ex);
        }
    }
