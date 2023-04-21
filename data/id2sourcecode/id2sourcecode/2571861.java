    public void run() {
        try {
            String nextLine = null;
            while (true) {
                if (stream_ == null) return;
                if (stream_.ready()) {
                    nextLine = stream_.readLine();
                    if (nextLine.startsWith(VM_CRASH_TEXT)) execProcess_.vmcrash();
                    if (nextLine != null) {
                        if (fileLogging_) {
                            trace_.println(nextLine);
                            trace_.flush();
                        }
                        if (!(pattern_.equals("")) && (nextLine.indexOf(pattern_) >= 0)) {
                            fileOut_.getFD().sync();
                            logger_.log(Level.FINE, "Pattern found: " + pattern_);
                            execProcess_.patternMatchFound(traceFile_);
                        }
                        if (showOutput_) logger_.log(Level.FINE, name_ + ": " + nextLine);
                    } else {
                        return;
                    }
                } else {
                    synchronized (syncObj_) {
                        if (!thread_running_) return;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        } catch (IOException ex) {
        } finally {
            synchronized (syncObj_) {
                done_ = true;
                syncObj_.notifyAll();
            }
        }
    }
