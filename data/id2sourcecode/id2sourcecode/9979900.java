        public long getFilePointer() throws IOException {
            throw new IOException("Do not call this method because read & writes are buffered.");
        }
