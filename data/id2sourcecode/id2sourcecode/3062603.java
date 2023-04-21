        public int read() throws IOException {
            if (readIndex < writeIndex) return bytes[readIndex++]; else throw new EOFException("Read buffer not available.");
        }
