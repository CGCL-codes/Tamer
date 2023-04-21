        public long save(ByteChannel channel, long timeout) throws IOException {
            long res = 0;
            long r = getNextReadIndex(timeout, mark);
            if (mark > r) {
                return res;
            }
            if (mark < minIndex) {
                throw new BufferUnderflowException();
            }
            int k;
            if (mark < 0) {
                k = 0;
            } else {
                k = (int) (mark % bufferSize);
            }
            int c = (int) (r - mark);
            for (int i = 0; i < c; i++) {
                int x = k + i;
                if (x == bufferSize) x = 0;
                readBuffer.limit(x * streamMaxSize + lengths[x]);
                readBuffer.position(x * streamMaxSize);
                channel.write(readBuffer);
                res += lengths[x];
            }
            if (mark < minIndex) {
                throw new BufferUnderflowException();
            }
            mark = r;
            return res;
        }
