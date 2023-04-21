        public boolean writeRequest(String oid, TypeDescription type, String function, ThreadId tid, Object[] arguments) throws IOException {
            return protocol.writeRequest(oid, type, function, tid, arguments);
        }
