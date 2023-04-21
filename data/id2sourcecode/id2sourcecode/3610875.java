    public void sendReply(boolean exception, ThreadId threadId, Object result) {
        if (DEBUG) {
            System.err.println("##### " + getClass().getName() + ".sendReply: " + exception + " " + result);
        }
        checkDisposed();
        try {
            _iProtocol.writeReply(exception, threadId, result);
        } catch (IOException e) {
            dispose(e);
            throw new DisposedException("unexpected " + e);
        } catch (RuntimeException e) {
            dispose(e);
            throw e;
        } catch (Error e) {
            dispose(e);
            throw e;
        }
    }
