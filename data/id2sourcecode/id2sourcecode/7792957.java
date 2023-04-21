        private boolean waitForMore(final boolean forceBlock) throws IOException {
            if (!forceBlock && !this.blocking) return false;
            this.sink.lock();
            try {
                if (!forceBlock && !this.blocking) return false; else if (this.sink.isOpen()) {
                    if (this.fileChannel == null) return false;
                    if (this.readPosition != this.sink.writeCount()) return true;
                    try {
                        this.sink.notEmpty.await();
                        return true;
                    } catch (InterruptedException ex) {
                        ex = null;
                        IO.tryClose(this);
                        Thread.currentThread().interrupt();
                        throw new ClosedByInterruptException();
                    }
                } else if (this.fileChannel == null) return false; else if (this.readPosition != this.sink.writeCount()) return true; else {
                    closeFile();
                    return false;
                }
            } finally {
                this.sink.unlock();
            }
        }
