        private void writeCompleteFile(InputStream file, long size) throws IOException {
            int count = 0;
            int read;
            try {
                while (count < size) {
                    read = file.read(buffer, 0, (int) (((size - count) < buffer.length) ? (size - count) : buffer.length));
                    if (read == -1) {
                        throw new EOFException("SCP received an unexpected EOF");
                    }
                    count += read;
                    out.write(buffer, 0, read);
                }
            } finally {
                file.close();
            }
        }
