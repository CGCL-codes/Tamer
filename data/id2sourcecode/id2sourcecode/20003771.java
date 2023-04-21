        public void writeReply(boolean exception, ThreadId tid, Object result) throws IOException {
            protocol.writeReply(exception, tid, result);
        }
