    private void doWrite(BufferedWriter w, Message m, SizedIterable<String> text) {
        Long threadId = m.thread().getId();
        String indentString;
        synchronized (_indenters) {
            indentString = _indenters.get(threadId).indentString();
        }
        Runnable unlock = _locks.lock(w);
        try {
            if (_lastThreads.containsKey(w)) {
                Long prevId = _lastThreads.get(w);
                if (!prevId.equals(threadId)) {
                    w.newLine();
                    _lastThreads.put(w, threadId);
                }
            } else {
                _lastThreads.put(w, threadId);
            }
            w.write(indentString);
            w.write("[" + formatLocation(m.caller()) + " - " + formatThread(m.thread()) + " - " + formatTime(m.time()) + "]");
            w.newLine();
            for (String s : text) {
                w.write(indentString);
                w.write(HANGING_INDENT);
                w.write(s);
                w.newLine();
            }
            w.flush();
        } catch (IOException e) {
            throw new WrappedException(e);
        } finally {
            unlock.run();
        }
    }
