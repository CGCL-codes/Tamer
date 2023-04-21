    synchronized DocumentsWriterThreadState getThreadState(Document doc, Term delTerm) throws IOException {
        DocumentsWriterThreadState state = (DocumentsWriterThreadState) threadBindings.get(Thread.currentThread());
        if (state == null) {
            DocumentsWriterThreadState minThreadState = null;
            for (int i = 0; i < threadStates.length; i++) {
                DocumentsWriterThreadState ts = threadStates[i];
                if (minThreadState == null || ts.numThreads < minThreadState.numThreads) minThreadState = ts;
            }
            if (minThreadState != null && (minThreadState.numThreads == 0 || threadStates.length >= MAX_THREAD_STATE)) {
                state = minThreadState;
                state.numThreads++;
            } else {
                DocumentsWriterThreadState[] newArray = new DocumentsWriterThreadState[1 + threadStates.length];
                if (threadStates.length > 0) System.arraycopy(threadStates, 0, newArray, 0, threadStates.length);
                state = newArray[threadStates.length] = new DocumentsWriterThreadState(this);
                threadStates = newArray;
            }
            threadBindings.put(Thread.currentThread(), state);
        }
        waitReady(state);
        initSegmentName(false);
        state.isIdle = false;
        boolean success = false;
        try {
            state.docState.docID = nextDocID;
            assert writer.testPoint("DocumentsWriter.ThreadState.init start");
            if (delTerm != null) {
                addDeleteTerm(delTerm, state.docState.docID);
                state.doFlushAfter = timeToFlushDeletes();
            }
            assert writer.testPoint("DocumentsWriter.ThreadState.init after delTerm");
            nextDocID++;
            numDocsInRAM++;
            if (!flushPending && maxBufferedDocs != IndexWriter.DISABLE_AUTO_FLUSH && numDocsInRAM >= maxBufferedDocs) {
                flushPending = true;
                state.doFlushAfter = true;
            }
            success = true;
        } finally {
            if (!success) {
                state.isIdle = true;
                notifyAll();
                if (state.doFlushAfter) {
                    state.doFlushAfter = false;
                    flushPending = false;
                }
            }
        }
        return state;
    }
