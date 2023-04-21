    void run(java.io.Reader reader, java.io.Writer writer) {
        if (!preTransform()) return;
        outputStream = createStreamResult(writer);
        transformWithSAX(reader);
    }
